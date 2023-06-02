package academy.doku.da3duawebserviceapi.mekaniku.user.controller;

import academy.doku.da3duawebserviceapi.common.utils.CheckRoleUtil;
import academy.doku.da3duawebserviceapi.mekaniku.user.dto.*;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserRole;
import academy.doku.da3duawebserviceapi.mekaniku.user.exception.EmailNotFoundException;
import academy.doku.da3duawebserviceapi.mekaniku.user.service.TokenService;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserEntity;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserSecurity;
import academy.doku.da3duawebserviceapi.mekaniku.user.service.impl.JpaUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final JpaUserDetailService jpaUserDetailService;

    @CrossOrigin(origins = "*",allowedHeaders = "*")
    @PostMapping("/register/merchants")
    public ResponseEntity<ResponseDTO> addUser(@RequestBody UserDTO userDTO) {
        tokenService.addUser(userDTO);
        return new ResponseEntity<ResponseDTO>(ResponseDTO.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("success add user")
                .data(userDTO)
                .build(), HttpStatus.CREATED);
    }

    //add error handling
    @CrossOrigin(origins = "*",allowedHeaders = "*")
    @PostMapping("/login")
    public ResponseEntity<loginDTO> token(@RequestBody UserEntity userEntity) {
//        try {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEntity.getEmail(), userEntity.getPassword()));

        String token = tokenService.generatedToken(authentication, userEntity.getEmail());
        UserSecurity user = (UserSecurity) authentication.getPrincipal();

        loginDTO response = new loginDTO();
        response.setToken(token);
        response.setRole(user.getRole().toString());

        return new ResponseEntity(ResponseDTO.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login Success")
                .data(response)
                .build(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*",allowedHeaders = "*")
    @GetMapping("/user-data-2")
    public ResponseEntity<UserDTO> userInfo2(@RequestHeader(name = "Authorization") String tokenBearer) {
        UserDTO user = tokenService.    decodeToken(tokenBearer);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*",allowedHeaders = "*")
    @GetMapping("/users")
    public ResponseEntity<ResponseDTO<PaginateUserDTO>> getAllUsers(
            @RequestParam(name = "role", required = false) UserRole role,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PaginateUserDTO userDTOs = jpaUserDetailService.getAllUser(role, pageable);

        return new ResponseEntity<>(ResponseDTO.<PaginateUserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get All User")
                .data(userDTOs)
                .build(), HttpStatus.OK);
        //return ResponseEntity.ok(userDTOs);
    }

    @CrossOrigin(origins = "*",allowedHeaders = "*")
    @GetMapping("/profile")
    public ResponseEntity<ResponseDTO> userInfo2(Principal principal) {

        Integer userId = Integer.valueOf(principal.getName());
        ProfilDTO profilDTO = jpaUserDetailService.getUserById(userId);
        return new ResponseEntity<ResponseDTO>(ResponseDTO.builder()
                .statusCode(HttpStatus.OK.value())
                .message("success show user")
                .data(profilDTO)
                .build(), HttpStatus.OK);
    }

    @PatchMapping("/password/{id}")
    public ResponseEntity<ResponseDTO<ChangePasswordDTO>> changePasswordById(
            @RequestBody ChangePasswordDTO changePasswordDTO,
            @PathVariable("id") Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkMerchantOnlyAccess(authentication);
        ChangePasswordDTO result = tokenService.changePass(changePasswordDTO, id);
        ResponseDTO<ChangePasswordDTO> response = new ResponseDTO<>(
                HttpStatus.OK.value(),
                "Password changed successfully",
                null
        );
        return ResponseEntity.ok(response);
    }

}
