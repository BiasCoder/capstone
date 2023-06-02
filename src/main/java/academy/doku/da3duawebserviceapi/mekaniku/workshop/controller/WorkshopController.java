package academy.doku.da3duawebserviceapi.mekaniku.workshop.controller;


import academy.doku.da3duawebserviceapi.common.utils.CheckRoleUtil;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.*;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.WorkshopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/workshops")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WorkshopController {
    private final WorkshopService workshopService;


    @PostMapping
    public ResponseEntity<ResponseDTO> createMerchant (Principal principal,
                                                       @RequestBody WorkshopDTO workshopDTO){

        Integer userId = Integer.valueOf(principal.getName());

        return new ResponseEntity<ResponseDTO>(ResponseDTO.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Merchant Created")
                .data(workshopService.createMerchant(userId, workshopDTO))
                .build(), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getWorkshopById(@PathVariable Integer id) {
        WorkshopDetailDTO workshopDTO = workshopService.getById(id);
        ResponseDTO responseDTO = new ResponseDTO(HttpStatus.OK.value(), "Show Workshop", workshopDTO);
        return ResponseEntity.ok(responseDTO);
    }

    //superadmin
    @GetMapping()
    public ResponseEntity<ResponseDTO<PaginateWorkshopDTO>> getAllWorkshop(Pageable pageable){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkSuperAdminOnlyAccess(authentication);
        PaginateWorkshopDTO paginateWorkshopDTO = workshopService.getAllWorkshopWithPagination(pageable);
        return new ResponseEntity<>(ResponseDTO
                .<PaginateWorkshopDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get All Workshop")
                .data(paginateWorkshopDTO)
                .build(), HttpStatus.OK);
    }

    @PatchMapping("profile/{id}")
    public ResponseEntity<ResponseDTO<UpdateProfilWorkshopDTO>> updateWorkshop(@PathVariable Integer id, @RequestBody UpdateProfilWorkshopDTO updateProfilWorkshopDTO){

        return new ResponseEntity<>(ResponseDTO.<UpdateProfilWorkshopDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Update Profile Success")
                .data(workshopService.updateWorkshop(updateProfilWorkshopDTO, id))
                .build(), HttpStatus.OK);
    }


//superadmin
    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDTO<SuspendDTO>> changeStatusUser(@PathVariable Integer id, @RequestBody SuspendDTO suspendDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkSuperAdminOnlyAccess(authentication);

        return new ResponseEntity<>(ResponseDTO.<SuspendDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Change Status Success")
                .data(workshopService.changeStatus(suspendDTO, id))
                .build(),HttpStatus.CREATED);
    }


    @PatchMapping("desc/{id}")
    public ResponseEntity<ResponseDTO<DescDTO>> changeDesc(@PathVariable Integer id, @RequestBody DescDTO descDTO){

        return new ResponseEntity<>(ResponseDTO.<DescDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Description Updated")
                .data(workshopService.changeDesc(descDTO, id))
                .build(), HttpStatus.CREATED);
    }


}
