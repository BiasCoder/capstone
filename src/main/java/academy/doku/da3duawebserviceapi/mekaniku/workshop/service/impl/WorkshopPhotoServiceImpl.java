package academy.doku.da3duawebserviceapi.mekaniku.workshop.service.impl;

import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserEntity;
import academy.doku.da3duawebserviceapi.mekaniku.user.repository.UserRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.AvatarResponse;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.WorkshopPhotoResponse;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopPhotoEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.exception.DeletePhotoFailedException;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.exception.UploadPhotoFailedException;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.repository.WorkshopPhotoRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.repository.WorkshopRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.DeleteWorkshopAvatarService;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.DeleteWorkshopPhotoService;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.UploadWorkshopAvatarService;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.UploadWorkshopPhotoService;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.util.AmazonS3Utility;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkshopPhotoServiceImpl implements  UploadWorkshopPhotoService, DeleteWorkshopPhotoService, UploadWorkshopAvatarService, DeleteWorkshopAvatarService {

    private final UserRepository userRepository;
    private final WorkshopPhotoRepository workshopPhotoRepository;
    private final WorkshopRepository workshopRepository;

    private final ModelMapper modelMapper;

    private final AmazonS3Utility storageUtils;

    @Override
    public WorkshopPhotoResponse uploadPhoto(Integer userId, MultipartFile file) {

        WorkshopEntity workshop = workshopRepository.findByUserId(userId).orElseThrow(
                () -> new RuntimeException("workshop not found")
        );

        WorkshopPhotoEntity photo = new WorkshopPhotoEntity();

        try {
            String uploadedPhotoUrl = storageUtils.uploadFile(file, "workshop");
            photo.setWorkshop(workshop);
            photo.setPhotoUrl(uploadedPhotoUrl);
        } catch (Exception e) {
            throw new UploadPhotoFailedException("Upload Failed! ERROR: " + e.getMessage());
        }

        return modelMapper.map(workshopPhotoRepository.save(photo), WorkshopPhotoResponse.class);
    }

    @Override
    public void deletePhoto(UUID workshopPhotoId) {
        WorkshopPhotoEntity photo = workshopPhotoRepository.findById(workshopPhotoId).orElseThrow();
        try {
            storageUtils.deleteFileFromS3Bucket(photo.getPhotoUrl(), "workshop");
        } catch (Exception e) {
            throw new DeletePhotoFailedException("Delete Photo Failed! ERROR: " + e.getMessage());
        }

        workshopPhotoRepository.deleteById(workshopPhotoId);
    }

    @Override
    public AvatarResponse uploadAvatar(Integer userId, MultipartFile avatar) {
        UserEntity owner = userRepository.findById(userId).orElseThrow();

        if (owner.getAvatarUrl() != null) {
            try {
                storageUtils.deleteFileFromS3Bucket(owner.getAvatarUrl(), "workshop/avatar");
            } catch (Exception e) {
                throw new DeletePhotoFailedException("Delete Avatar Failed! ERROR: " + e.getMessage());
            }
        }

        try {
            String uploadedPhotoUrl = storageUtils.uploadFile(avatar, "workshop/avatar");
            owner.setAvatarUrl(uploadedPhotoUrl);
        } catch (Exception e) {
            throw new UploadPhotoFailedException("Upload Avatar Failed! ERROR: " + e.getMessage());
        }

        return modelMapper.map(userRepository.save(owner), AvatarResponse.class);
    }

    @Override
    public void deleteAvatar(Integer userId) {
        UserEntity owner = userRepository.findById(userId).orElseThrow();

        try {
            storageUtils.deleteFileFromS3Bucket(owner.getAvatarUrl(), "workshop/avatar");
        } catch (Exception e) {
            throw new DeletePhotoFailedException("Delete Avatar Failed! ERROR: " + e.getMessage());
        }

        owner.setAvatarUrl(null);
        userRepository.save(owner);
    }
}
