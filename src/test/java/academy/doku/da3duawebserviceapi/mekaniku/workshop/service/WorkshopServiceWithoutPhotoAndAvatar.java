import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.UserContactDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserEntity;
import academy.doku.da3duawebserviceapi.mekaniku.user.repository.UserRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.WorkshopDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.WorkshopDetailDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.WorkshopPhotoResponse;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopPhotoEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.repository.WorkshopPhotoRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.repository.WorkshopRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.impl.WorkshopServiceImpl;

public class WorkshopServiceWithoutPhotoAndAvatar {

    @InjectMocks
    private WorkshopServiceImpl workshopService;

    @Mock
    private WorkshopRepository workshopRepository;

    @Mock
    private WorkshopPhotoRepository workshopPhotoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateMerchant() {
        UserEntity user = new UserEntity();
        user.setId(1);

        WorkshopDTO workshopDTO = new WorkshopDTO();
        workshopDTO.setName("Test Workshop");
        workshopDTO.setAddress("Test Address");

        WorkshopEntity workshop = new WorkshopEntity();
        workshop.setId(1);
        workshop.setName("Test Workshop");
        workshop.setAddress("Test Address");
        workshop.setUser(user);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(workshopRepository.findByUserId(1)).thenReturn(Optional.empty());
        when(modelMapper.map(workshopDTO, WorkshopEntity.class)).thenReturn(workshop);
        when(workshopRepository.save(workshop)).thenReturn(workshop);
        when(modelMapper.map(workshop, WorkshopDTO.class)).thenReturn(workshopDTO);

        WorkshopDTO result = workshopService.createMerchant(1, workshopDTO);

        assertEquals(workshopDTO.getName(), result.getName());
        assertEquals(workshopDTO.getAddress(), result.getAddress());
    }

//    @Test
//    void testGetById() {
//        // create sample workshop data
//        WorkshopEntity workshop = new WorkshopEntity();
//        workshop.setId(1);
//        workshop.setName("Sample Workshop");
//        workshop.setAddress("Sample Address");
//
//        // mock the repository method
//        when(workshopRepository.findById(anyInt())).thenReturn(Optional.of(workshop));
//
//        // call the method to be tested
//        WorkshopDetailDTO workshopDetailDTO = workshopService.getById(1);
//
//        // verify the result
//        assertNotNull(workshopDetailDTO);
//        assertEquals(workshop.getName(), workshopDetailDTO.getName());
//        assertEquals(workshop.getAddress(), workshopDetailDTO.getAddress());
//    }





}