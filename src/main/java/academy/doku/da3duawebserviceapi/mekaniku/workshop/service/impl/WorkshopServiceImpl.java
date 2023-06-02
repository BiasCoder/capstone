package academy.doku.da3duawebserviceapi.mekaniku.workshop.service.impl;

import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserEntity;
import academy.doku.da3duawebserviceapi.mekaniku.user.repository.UserRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.*;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopPhotoEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.repository.WorkshopPhotoRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.repository.WorkshopRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.WorkshopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class WorkshopServiceImpl implements WorkshopService {

    private final WorkshopRepository workshopRepository;

    private final WorkshopPhotoRepository workshopPhotoRepository;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public WorkshopDTO createMerchant(Integer userId, WorkshopDTO workshopDTO) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow();
        Optional<WorkshopEntity> workshop1 = workshopRepository.findByUserId(userId);
        if(workshop1.isPresent()){
            throw new RuntimeException("User sudah pernah mendaftar");
        }
        WorkshopEntity workshop = modelMapper.map(workshopDTO, WorkshopEntity.class);
        workshop.setUser(user);
        WorkshopEntity savedWorkshop = workshopRepository.save(workshop);
        System.out.println(savedWorkshop);
        WorkshopDTO workshopResult = modelMapper.map(savedWorkshop, WorkshopDTO.class);
        return workshopResult;
    }

    @Override
    public WorkshopDetailDTO getById(Integer id) {
        Optional<WorkshopEntity> workshop = workshopRepository.findById(id);
        if (!workshop.isPresent()) {
            throw new RuntimeException("Not Found");
        }
        WorkshopDetailDTO workshopDTO = modelMapper.map(workshop.get(), WorkshopDetailDTO.class);
        List<WorkshopPhotoEntity> workshopPhotos = workshopPhotoRepository.findByWorkshopId(id);
        List<WorkshopPhotoResponse> workshopPhotoResponses = workshopPhotos.stream()
                .map(photo -> modelMapper.map(photo, WorkshopPhotoResponse.class))
                .collect(Collectors.toList());
        workshopDTO.setWorkshopPhotos(workshopPhotoResponses);

        workshopDTO.setUser(modelMapper.map(workshop.get().getUser(), UserContactDTO.class));
        return workshopDTO;
    }

    @Override
    public PaginateWorkshopDTO getAllWorkshopWithPagination(Pageable pageable) {
        Page<WorkshopEntity> workshops = workshopRepository.findAll(pageable);
        List<WorkshopDetailDTO> List = new ArrayList<>();
        for (WorkshopEntity workshop : workshops.getContent()) {
            WorkshopDetailDTO workshopDetailDTO = modelMapper.map(workshop, WorkshopDetailDTO.class);
            List.add(workshopDetailDTO);
        }
        return PaginateWorkshopDTO.builder().List(List).totalItems(workshops.getTotalElements())
                .totalPage(Long.valueOf(workshops.getTotalPages())).currentPage(Long.valueOf(workshops.getNumber())).build();
    }

    @Override
    public WorkshopDetailDTO findByUserId(Integer userId) {
        var workshop = workshopRepository.findByUserId(userId).orElseThrow();
        var id = workshop.getId();
        return getById(id);
    }


    @Override
    public UpdateProfilWorkshopDTO updateWorkshop(UpdateProfilWorkshopDTO updateProfilWorkshopDTO, Integer id) {
        Optional<WorkshopEntity> optionalWorkshopEntity = workshopRepository.findById(id);
        if (!optionalWorkshopEntity.isPresent()){
            throw new RuntimeException("Not Found");
        }
        WorkshopEntity workshop = optionalWorkshopEntity.get();
        workshop.setName(updateProfilWorkshopDTO.getName());
        workshop.setAddress(updateProfilWorkshopDTO.getAddress());
        workshop.setLatitude(updateProfilWorkshopDTO.getLatitude());
        workshop.setLongitude(updateProfilWorkshopDTO.getLongitude());
        UserEntity user = workshop.getUser();
        user.setPhone(updateProfilWorkshopDTO.getPhone());

        userRepository.save(user);
        workshopRepository.save(workshop);

        return updateProfilWorkshopDTO;
    }

    @Override
    public SuspendDTO changeStatus(SuspendDTO suspendDTO, Integer id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (!optionalUserEntity.isPresent()){
            throw new RuntimeException("Not Found");
        }
        UserEntity user = optionalUserEntity.get();
        user.setSuspend(suspendDTO.getSuspend());
        userRepository.save(user);
        return suspendDTO;
    }

    @Override
    public DescDTO changeDesc(DescDTO descDTO, Integer id) {
        Optional<WorkshopEntity> optionalWorkshopEntity = workshopRepository.findById(id);
        if (!optionalWorkshopEntity.isPresent()){
            throw new RuntimeException("Not Found");
        }
        WorkshopEntity workshop = optionalWorkshopEntity.get();
        workshop.setDescription(descDTO.getDescription());
        workshopRepository.save(workshop);
        return descDTO;
    }
}
