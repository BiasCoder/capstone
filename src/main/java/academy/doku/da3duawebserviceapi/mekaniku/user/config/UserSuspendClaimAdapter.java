package academy.doku.da3duawebserviceapi.mekaniku.user.config;

import academy.doku.da3duawebserviceapi.mekaniku.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserSuspendClaimAdapter implements Converter<Map<String, Object>, Map<String, Object>> {

    private final MappedJwtClaimSetConverter delegate = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());
    private final UserRepository userRepository;

    @Override
    public Map<String, Object> convert(@NonNull Map<String, Object> claims) {
        Map<String, Object> convertedClaims = delegate.convert(claims);

        String email = (String) convertedClaims.get("email");
        var userDTO = userRepository.findByEmail(email).orElseThrow();

        convertedClaims.put("suspend", userDTO.getSuspend());
        return convertedClaims;
    }
}
