//package academy.doku.da3duawebserviceapi.mekaniku.user.config;
//
//
//import io.jsonwebtoken.io.IOException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthorizationFilter extends OncePerRequestFilter {
//
//    private final JwtProvider jwtProvider;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
//        try {
//            String token = getTokenFromRequest(request);
//            if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
//                String username = jwtProvider.getUsernameFromToken(token);
//                List<String> roles = jwtProvider.getRolesFromToken(token); // mengambil roles dari token
//
//                // disini dapat dilakukan pengecekan apakah user memiliki akses ke endpoint tertentu berdasarkan roles-nya
//
//                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, getAuthorities(roles));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        } catch (Exception ex) {
//            logger.error("Error authenticating user", ex);
//            SecurityContextHolder.clearContext();
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    private List<GrantedAuthority> getAuthorities(List<String> roles) {
//        return roles.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//    }
//
//    private String getTokenFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//}
//
