package academy.doku.da3duawebserviceapi.mekaniku.workshop.controller;

import academy.doku.da3duawebserviceapi.common.utils.CheckRoleUtil;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.AvatarResponse;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.ResponseDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.WorkshopPhotoResponse;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.DeleteWorkshopAvatarService;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.DeleteWorkshopPhotoService;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.UploadWorkshopAvatarService;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.UploadWorkshopPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WorkshopPhotoController {

    private final UploadWorkshopPhotoService uploadWorkshopPhotoService;
    private final UploadWorkshopAvatarService uploadWorkshopAvatarService;
    private final DeleteWorkshopPhotoService deleteWorkshopPhotoService;
    private final DeleteWorkshopAvatarService deleteWorkshopAvatarService;


    @PostMapping("workshops/photos")
    public ResponseEntity<ResponseDTO<WorkshopPhotoResponse>> uploadWorkshopPhoto(
            @RequestPart(value = "file") MultipartFile photo
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkMerchantOnlyAccess(authentication);

        Jwt token = (Jwt) authentication.getPrincipal();

        Integer userId = Integer.valueOf(token.getClaim("id").toString());

        return new ResponseEntity<>(ResponseDTO.<WorkshopPhotoResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Workshop photo uploaded")
                .data(uploadWorkshopPhotoService.uploadPhoto(userId, photo))
                .build(), HttpStatus.CREATED);
    }


    @PatchMapping("workshops/avatar")
    public ResponseEntity<ResponseDTO<AvatarResponse>> uploadWorkshopAvatar(
            @RequestPart(value = "file") MultipartFile avatar
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkMerchantOnlyAccess(authentication);
        Jwt token = (Jwt) authentication.getPrincipal();

        Integer userId = Integer.valueOf(token.getClaim("id").toString());
        return new ResponseEntity<>(ResponseDTO.<AvatarResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Workshop avatar uploaded")
                .data(uploadWorkshopAvatarService.uploadAvatar(userId, avatar))
                .build(), HttpStatus.CREATED);
    }

    @DeleteMapping("workshops/photos/{workshopPhotoId}")
    public ResponseEntity<ResponseDTO<WorkshopPhotoResponse>> deleteWorkshopPhoto(
            @PathVariable UUID workshopPhotoId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkMerchantOnlyAccess(authentication);

        deleteWorkshopPhotoService.deletePhoto(workshopPhotoId);
        return new ResponseEntity<>(ResponseDTO.<WorkshopPhotoResponse>builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .message("Workshop photo deleted")
                .build(), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("workshops/avatar")
    public ResponseEntity<ResponseDTO<AvatarResponse>> deleteWorkshopAvatar() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkMerchantOnlyAccess(authentication);
        Jwt token = (Jwt) authentication.getPrincipal();

        Integer userId = Integer.valueOf(token.getClaim("id").toString());
        deleteWorkshopAvatarService.deleteAvatar(userId);

        return new ResponseEntity<>(ResponseDTO.<AvatarResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Workshop avatar deleted")
                .build(), HttpStatus.OK);
    }
}
