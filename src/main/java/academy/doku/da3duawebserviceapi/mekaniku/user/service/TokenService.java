package academy.doku.da3duawebserviceapi.mekaniku.user.service;


import academy.doku.da3duawebserviceapi.mekaniku.user.dto.ChangePasswordDTO;
import academy.doku.da3duawebserviceapi.mekaniku.user.dto.UserDTO;
import org.springframework.security.core.Authentication;


public interface TokenService {
    String generatedToken(Authentication authentication, String email);
    UserDTO decodeToken(String token);
    UserDTO addUser(UserDTO userDTO);

    ChangePasswordDTO changePass (ChangePasswordDTO changePasswordDTO, Integer id);

}
