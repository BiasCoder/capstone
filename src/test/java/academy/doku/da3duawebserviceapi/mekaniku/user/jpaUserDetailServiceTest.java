package academy.doku.da3duawebserviceapi.mekaniku.user;


import academy.doku.da3duawebserviceapi.common.utils.CheckRoleUtil;
import academy.doku.da3duawebserviceapi.mekaniku.user.controller.AuthController;
import academy.doku.da3duawebserviceapi.mekaniku.user.dto.*;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserEntity;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserRole;
import academy.doku.da3duawebserviceapi.mekaniku.user.repository.UserRepository;
import academy.doku.da3duawebserviceapi.mekaniku.user.service.TokenService;
import academy.doku.da3duawebserviceapi.mekaniku.user.service.impl.JpaUserDetailService;
import academy.doku.da3duawebserviceapi.mekaniku.user.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class jpaUserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Principal mockPrincipal;

    @Mock
    private ModelMapper modelMapper;



    @InjectMocks
    private JpaUserDetailService userDetailService;


    @Mock
    private CheckRoleUtil checkRoleUtil;

    @Mock
    private AuthController authController;

    TokenService tokenService = Mockito.mock(TokenService.class);


    @Test
    public void testLoadUserByUsername() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("john.doe@example.com");
        userEntity.setPassword("password");

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = userDetailService.loadUserByUsername("john.doe@example.com");

        assertNotNull(userDetails);
        assertEquals(userEntity.getEmail(), userDetails.getUsername());
        assertEquals(userEntity.getPassword(), userDetails.getPassword());
    }

    @Test
    public void testLoadUserByUsernameThrowsException() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailService.loadUserByUsername("john.doe@example.com");
        });
    }

    @Test
    public void testGetAllUser() {
        // mock data
        List<UserEntity> userList = new ArrayList<>();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(20);
        userEntity.setEmail("john.doe@example.com");
        userEntity.setRole(UserRole.WORKSHOP);
        userList.add(userEntity);

        // mock repository method call
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserEntity> userPage = new PageImpl<>(userList, pageable, userList.size());
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        // mock model mapper
        GetAllUserDTO getAllUserDTO = new GetAllUserDTO();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(20);
        userDTO.setEmail("john.doe@example.com");
        userDTO.setRole(String.valueOf(UserRole.WORKSHOP));
        when(modelMapper.map(any(UserEntity.class), eq(GetAllUserDTO.class))).thenReturn(getAllUserDTO);

        //when(modelMapper.map(userEntity, UserDTO.class)).thenReturn(userDTO);

        // test the service method
        PaginateUserDTO result = userDetailService.getAllUser(null, pageable);
        System.out.println(userRepository.findAll(pageable));

        // assertions
        assertNotNull(result);
        assertEquals(userList.size(), result.getList().size());
        assertEquals(1L, result.getTotalPage());
        assertEquals(0L, result.getCurrentPage());
        assertEquals(userList.size(), result.getTotalItems());
        assertEquals(getAllUserDTO.getId(), result.getList().get(0).getId());
        assertEquals(getAllUserDTO.getEmail(), result.getList().get(0).getEmail());
        assertEquals(getAllUserDTO.getRole(), result.getList().get(0).getRole());
    }


    //blocker: tidak bisa mock final class
//    @Test
//    public void testChangePasswordById() throws Exception {
//        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("oldPassword", "newPassword");
//
//        Integer id = 1;
//        Authentication authentication = mock(Authentication.class);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        doNothing().when(checkRoleUtil).checkMerchantOnlyAccess(authentication);
//
//        ChangePasswordDTO result = new ChangePasswordDTO("oldPassword", "newPassword");
//        when(tokenService.changePass(changePasswordDTO, id)).thenReturn(result);
//
//        ResponseEntity<ResponseDTO<ChangePasswordDTO>> responseEntity = authController.changePasswordById(changePasswordDTO, id);
//
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals("Password changed successfully", responseEntity.getBody().getMessage());
//        assertNull(responseEntity.getBody().getData());
//        verify(checkRoleUtil, times(1)).checkMerchantOnlyAccess(authentication);
//        verify(tokenService, times(1)).changePass(changePasswordDTO, id);
//    }


}

