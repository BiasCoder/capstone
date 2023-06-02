package academy.doku.da3duawebserviceapi.mekaniku.workshop.service;

import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserEntity;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserRole;
import academy.doku.da3duawebserviceapi.mekaniku.user.repository.UserRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.AvatarResponse;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.WorkshopPhotoResponse;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopPhotoEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.repository.WorkshopPhotoRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.repository.WorkshopRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.impl.WorkshopPhotoServiceImpl;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.util.AmazonS3Utility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WorkshopServiceImplTest {

    @InjectMocks
    private WorkshopPhotoServiceImpl workshopPhotoService;

    @Mock
    private  UserRepository userRepository;

    @Mock
    private WorkshopPhotoRepository workshopPhotoRepository;

    @Mock
    private WorkshopRepository workshopRepository;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @Mock
    private AmazonS3Utility storageUtils;

    @Test
    void upload_workshop_photo() {
        Integer workshopId = 1;
        UUID workshopPhotoId = UUID.fromString("d1d25ee5-43a0-4ce7-a677-38b634be6d75");

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.MARCH, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.MARCH, 19, 10, 10);

        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(workshopId, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);

        WorkshopPhotoEntity expectedData = new WorkshopPhotoEntity(workshopPhotoId, workshop, "http://mekaniku.com/photo.png", createdDate, modifiedDate);

        when(workshopRepository.findByUserId(workshopOwner.getId())).thenReturn(Optional.of(workshop));
        when(workshopPhotoRepository.save(any(WorkshopPhotoEntity.class))).thenReturn(expectedData);

        WorkshopPhotoResponse uploadedPhoto = workshopPhotoService.uploadPhoto(workshopOwner.getId(), any(MultipartFile.class));

        verify(workshopRepository, times(1)).findByUserId(workshopOwner.getId());
        verify(workshopPhotoRepository, times(1)).save(any(WorkshopPhotoEntity.class));

        assertEquals(expectedData.getId(), uploadedPhoto.getId());
        assertEquals(expectedData.getPhotoUrl(), uploadedPhoto.getPhotoUrl());
    }

    @Test
    void upload_workshop_avatar() {
        Integer userId = 1;

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.MARCH, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.MARCH, 19, 10, 10);

        UserEntity workshopOwner = new UserEntity(userId, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", null, UserRole.WORKSHOP, false, createdDate, modifiedDate);
        UserEntity expectedData = new UserEntity(userId, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "photo.jpg", UserRole.WORKSHOP, false, createdDate, modifiedDate);

        when(userRepository.findById(userId)).thenReturn(Optional.of(workshopOwner));
        when(userRepository.save(any(UserEntity.class))).thenReturn(expectedData);

        AvatarResponse uploadedAvatar = workshopPhotoService.uploadAvatar(userId, any(MultipartFile.class));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(UserEntity.class));

        assertEquals(expectedData.getId(), uploadedAvatar.getId());
        assertEquals(expectedData.getAvatarUrl(), uploadedAvatar.getAvatar_url());
    }

    @Test
    void change_workshop_avatar() {
        Integer userId = 1;

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.MARCH, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.MARCH, 19, 10, 10);

        UserEntity workshopOwner = new UserEntity(userId, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "old_photo.jpg", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        UserEntity expectedData = new UserEntity(userId, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "new_photo.jpg", UserRole.WORKSHOP, false, createdDate, modifiedDate);

        when(userRepository.findById(userId)).thenReturn(Optional.of(workshopOwner));
        when(userRepository.save(any(UserEntity.class))).thenReturn(expectedData);

        AvatarResponse uploadedAvatar = workshopPhotoService.uploadAvatar(userId, any(MultipartFile.class));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(UserEntity.class));

        assertEquals(expectedData.getId(), uploadedAvatar.getId());
        assertEquals(expectedData.getAvatarUrl(), uploadedAvatar.getAvatar_url());
    }

    @Test
    void delete_workshop_photo() {
        Integer workshopId = 1;
        UUID workshopPhotoId = UUID.fromString("d1d25ee5-43a0-4ce7-a677-38b634be6d75");

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.MARCH, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.MARCH, 19, 10, 10);

        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(workshopId, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);

        WorkshopPhotoEntity expectedData = new WorkshopPhotoEntity(workshopPhotoId, workshop, "http://mekaniku.com/photo.png", createdDate, modifiedDate);

        when(workshopPhotoRepository.findById(workshopPhotoId)).thenReturn(Optional.of(expectedData));

        workshopPhotoService.deletePhoto(workshopPhotoId);

        verify(workshopPhotoRepository, times(1)).findById(workshopPhotoId);
        verify(workshopPhotoRepository, times(1)).deleteById(workshopPhotoId);
    }

    @Test
    void delete_workshop_avatar() {
        Integer userId = 1;

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.MARCH, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.MARCH, 19, 10, 10);

        UserEntity workshopOwner = new UserEntity(userId, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "photo.jpg", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        UserEntity expectedData = new UserEntity(userId, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", null, UserRole.WORKSHOP, false, createdDate, modifiedDate);

        when(userRepository.findById(userId)).thenReturn(Optional.of(workshopOwner));
        when(userRepository.save(any(UserEntity.class))).thenReturn(expectedData);

        workshopPhotoService.deleteAvatar(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(workshopOwner);

        assertEquals(expectedData.getId(), workshopOwner.getId());
        assertEquals(expectedData.getAvatarUrl(), workshopOwner.getAvatarUrl());
    }
}