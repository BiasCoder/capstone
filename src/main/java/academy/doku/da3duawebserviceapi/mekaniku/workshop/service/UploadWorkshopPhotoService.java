package academy.doku.da3duawebserviceapi.mekaniku.workshop.service;

import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.WorkshopPhotoResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UploadWorkshopPhotoService {

    WorkshopPhotoResponse uploadPhoto(Integer userId, MultipartFile file);
}
