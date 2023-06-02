package academy.doku.da3duawebserviceapi.mekaniku.workshop.service;

import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.AvatarResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UploadWorkshopAvatarService {

    AvatarResponse uploadAvatar(Integer userId, MultipartFile avatar);
}
