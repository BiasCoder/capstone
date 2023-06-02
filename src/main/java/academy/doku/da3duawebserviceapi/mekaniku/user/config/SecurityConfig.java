package academy.doku.da3duawebserviceapi.mekaniku.user.config;


import academy.doku.da3duawebserviceapi.mekaniku.user.service.impl.JpaUserDetailService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private UserSuspendClaimAdapter userSuspendClaimAdapter;

    private final RSAKeyProperties rsaKeyProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .cors().and()
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(auth -> {
                    auth.antMatchers("/api/auth/login").permitAll();
                    auth.antMatchers("/api/auth/register/merchants").permitAll();
                    auth.antMatchers("/actuator/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint(

                        ))
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
                .build();
    }



    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {

        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return NimbusJwtDecoder.withPublicKey(rsaKeyProperties.getPublicKey()).build();
//    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeyProperties.getPublicKey()).privateKey(rsaKeyProperties.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    //mekanism status suspend
    @Bean
    public JwtDecoder jwtDecoder() {
        var jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaKeyProperties.getPublicKey()).build();
        jwtDecoder.setClaimSetConverter(userSuspendClaimAdapter);

        OAuth2TokenValidator<Jwt> userSuspendValidator = userSuspendValidator();
        jwtDecoder.setJwtValidator(userSuspendValidator);

        return jwtDecoder;
    }


    static class UserSuspendValidator implements OAuth2TokenValidator<Jwt> {
        OAuth2Error error = new OAuth2Error("user_suspended", "Your account has been suspended", null);

        @Override
        public OAuth2TokenValidatorResult validate(Jwt token) {
            if (token.getClaim("suspend").equals(false))
                return OAuth2TokenValidatorResult.success();
            else
                return OAuth2TokenValidatorResult.failure(error);
        }
    }

    OAuth2TokenValidator<Jwt> userSuspendValidator() {
        return new UserSuspendValidator();
    }

}
