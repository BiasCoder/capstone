package academy.doku.da3duawebserviceapi.mekaniku.user.service.impl;

import academy.doku.da3duawebserviceapi.mekaniku.user.dto.GetAllUserDTO;
import academy.doku.da3duawebserviceapi.mekaniku.user.dto.PaginateUserDTO;
import academy.doku.da3duawebserviceapi.mekaniku.user.dto.ProfilDTO;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserEntity;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserRole;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserSecurity;
import academy.doku.da3duawebserviceapi.mekaniku.user.dto.UserDTO;
import academy.doku.da3duawebserviceapi.mekaniku.user.repository.UserRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.PaginateWorkshopDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.WorkshopDetailDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.repository.WorkshopRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JpaUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;
    private final WorkshopRepository workshopRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return new UserSecurity(user.get());
        }
        throw new UsernameNotFoundException("email not found");
    }

    public ProfilDTO getUserById(Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Optional<WorkshopEntity> workshop = workshopRepository.findByUserId(id);

        ProfilDTO profile = modelMapper.map(userEntity, ProfilDTO.class);
        if (workshop.isPresent()) {
            profile.setWorkshopId(workshop.get().getId());
        }

        return profile;
    }

public PaginateUserDTO getAllUser(UserRole role, Pageable pageable) {
    Page<UserEntity> userPage;
    if (role != null) {
        userPage = userRepository.findAllByRole(role, pageable);
    } else {
        userPage = userRepository.findAll(pageable);
    }
    List<UserEntity> userList = userPage.getContent();

    List<GetAllUserDTO> dtoList = userList.stream().map(userEntity -> {
        GetAllUserDTO dto = modelMapper.map(userEntity, GetAllUserDTO.class);
        return dto;
    }).collect(Collectors.toList());

    return PaginateUserDTO.builder()
            .List(dtoList)
            .totalItems(userPage.getTotalElements())
            .totalPage(Long.valueOf(userPage.getTotalPages()))
            .currentPage(Long.valueOf(userPage.getNumber()))
            .build();
}



}
