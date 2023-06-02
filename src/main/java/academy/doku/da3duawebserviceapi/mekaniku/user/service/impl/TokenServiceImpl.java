package academy.doku.da3duawebserviceapi.mekaniku.user.service.impl;

import academy.doku.da3duawebserviceapi.mekaniku.user.dto.ChangePasswordDTO;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserEntity;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserSecurity;
import academy.doku.da3duawebserviceapi.mekaniku.user.exception.EmailNotFoundException;
import academy.doku.da3duawebserviceapi.mekaniku.user.exception.InvalidPasswordException;
import academy.doku.da3duawebserviceapi.mekaniku.user.service.TokenService;
import academy.doku.da3duawebserviceapi.mekaniku.user.util.PasswordValidator;
import academy.doku.da3duawebserviceapi.mekaniku.user.dto.UserDTO;
import academy.doku.da3duawebserviceapi.mekaniku.user.exception.EmailWasRegistered;
import academy.doku.da3duawebserviceapi.mekaniku.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;


//    @Override
//    public String generatedToken(Authentication authentication) {
//        Instant now = Instant.now();
//        UserSecurity user = (UserSecurity) authentication.getPrincipal();
//
//        JwtClaimsSet claims = JwtClaimsSet.builder()
//                .issuer("MekaniKu")
//                .issuedAt(now)
//                .expiresAt(now.plus(7, ChronoUnit.DAYS))
//                .subject(user.getId().toString())
//                .claim("id", user.getId())
//                .claim("email", user.getEmail())
//                .claim("role", user.getRole())
//                .claim("suspend", user.getSuspend())
//                .build();
//        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims))
//                .getTokenValue();
//    }

    @Override
    public String generatedToken(Authentication authentication, String email) {
        Instant now = Instant.now();
        UserSecurity user = (UserSecurity) authentication.getPrincipal();
        Optional<UserEntity> user1 = userRepository.findByEmail(email);
        if (!user1.isPresent()){
            throw new EmailNotFoundException("Email Not Found");
        }  if (!user.getPassword().equals(user1.get().getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("MekaniKu")
                .issuedAt(now)
                .expiresAt(now.plus(7, ChronoUnit.DAYS))
                .subject(user.getId().toString())
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .claim("suspend", user.getSuspend())
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }

    @Override
    public UserDTO decodeToken(String token) {
        String newToken = token.split(" ")[1];
        Jwt jwtToken = jwtDecoder.decode(newToken);
        String data = jwtToken.getSubject();
        Optional<UserEntity> user = userRepository.findByEmail(data);
        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserDTO.class);
        }
        throw new EmailNotFoundException("email not found");
    }

    @Override
    @Transactional(readOnly = false)
    public UserDTO addUser(UserDTO userDTO) {
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        if (!PasswordValidator.isValid(userEntity.getPassword())) {
            throw new InvalidPasswordException("error");
        }
        Optional<UserEntity> existingUser = userRepository.findByEmail(userEntity.getEmail());
        if (existingUser.isPresent()){
            throw new EmailWasRegistered("Email already exist");
        }
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        UserEntity savedUserEntity = userRepository.save(userEntity);
        userDTO.setId(Math.toIntExact(savedUserEntity.getId()));
        return userDTO;
    }

    @Override
    public ChangePasswordDTO changePass(ChangePasswordDTO changePasswordDTO, Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (encoder.matches(changePasswordDTO.getOldPassword(), userEntity.getPassword())) {
            String newPassword = encoder.encode(changePasswordDTO.getNewPassword());
            userEntity.setPassword(newPassword);
            userRepository.save(userEntity);
            return changePasswordDTO;
        } else {
            throw new RuntimeException("Incorrect password");
        }

    }
}
