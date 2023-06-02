package academy.doku.da3duawebserviceapi.mekaniku.user;

import academy.doku.da3duawebserviceapi.mekaniku.user.dto.GetAllUserDTO;
import academy.doku.da3duawebserviceapi.mekaniku.user.dto.PaginateUserDTO;
import academy.doku.da3duawebserviceapi.mekaniku.user.dto.UserDTO;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserRole;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserEntity;
import academy.doku.da3duawebserviceapi.mekaniku.user.repository.UserRepository;
import academy.doku.da3duawebserviceapi.mekaniku.user.service.impl.TokenServiceImpl;
import academy.doku.da3duawebserviceapi.mekaniku.user.util.PasswordValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class tokenServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JwtDecoder jwtDecoder;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    PasswordValidator passwordValidator;

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Test
    public void testAddUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFullname("John Doe");
        userDTO.setEmail("new-email@example.com");
        userDTO.setPhone("0814412389");
        userDTO.setRole("WORKSHOP");
        userDTO.setPassword("@Adxfc66");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(15);
        userEntity.setFullName(userDTO.getFullname());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPhone(userDTO.getPhone());
        userEntity.setRole(UserRole.valueOf(userDTO.getRole()));
        userEntity.setPassword(userDTO.getPassword());

        when(modelMapper.map(userDTO, UserEntity.class)).thenReturn(userEntity);
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserDTO result = tokenService.addUser(userDTO);

        verify(modelMapper).map(userDTO, UserEntity.class);
        verify(userRepository).findByEmail(userDTO.getEmail());
        verify(userRepository).save(userEntity);

        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals(userDTO.getFullname(), result.getFullname());
        assertEquals(userDTO.getPhone(), result.getPhone());
        assertEquals(userDTO.getRole(), result.getRole());
    }

    @Test
    public void testDecodeToken() {
        String token = "Bearer encoded_token";

        Jwt jwtToken = mock(Jwt.class);
        when(jwtDecoder.decode("encoded_token")).thenReturn(jwtToken);
        when(jwtToken.getSubject()).thenReturn("john.doe@example.com");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(15);
        userEntity.setFullName("John Doe");
        userEntity.setEmail("john.doe@example.com");
        userEntity.setPhone("0814412389");
        userEntity.setRole(UserRole.WORKSHOP);
        userEntity.setPassword("@Adxfc66");

        Optional<UserEntity> optionalUserEntity = Optional.of(userEntity);
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(optionalUserEntity);
        when(modelMapper.map(userEntity, UserDTO.class)).thenReturn(new UserDTO());

        UserDTO result = tokenService.decodeToken(token);

        verify(jwtDecoder).decode("encoded_token");
        verify(userRepository).findByEmail(userEntity.getEmail());
        verify(modelMapper).map(userEntity, UserDTO.class);

        assertNotNull(result);
    }



}