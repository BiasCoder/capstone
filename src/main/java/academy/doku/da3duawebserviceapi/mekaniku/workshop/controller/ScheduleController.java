package academy.doku.da3duawebserviceapi.mekaniku.workshop.controller;


import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.*;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.WorkshopScheduleService;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.WorkshopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/workshops")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ScheduleController {

    private final WorkshopScheduleService workshopScheduleService;

    private final WorkshopService workshopService;

    @PostMapping("/{id}/schedules")
    public ResponseEntity<ResponseDTO> addScheduleOnce(@PathVariable(value = "id") Integer workshopId,
                                             @Validated @RequestBody ScheduleDTO scheduleDTO) {
        AddScheduleResponseDTO response = workshopScheduleService.addScheduleOnce(workshopId, scheduleDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(HttpStatus.CREATED.value(), "Success", response));
    }

    @GetMapping("/schedules")
    public ResponseEntity<ResponseDTO<GetScheduleDTO>> getSchedule (Principal principal){
        Integer userId = Integer.valueOf(principal.getName());
        Integer id = workshopService.findByUserId(userId).getId();
        GetScheduleDTO getScheduleDTO = workshopScheduleService.getSchedule(id);
        return new ResponseEntity<>(ResponseDTO.<GetScheduleDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get Workshop Schedule")
                .data(getScheduleDTO)
                .build(), HttpStatus.OK);
    }

//    @GetMapping("/schedule/{id}")
//    public ResponseEntity<ResponseDTO<GetScheduleDTO>> getSchedule (@PathVariable Integer id){
//        GetScheduleDTO getScheduleDTO = workshopScheduleService.getSchedule(id);
//        return new ResponseEntity<>(ResponseDTO.<GetScheduleDTO>builder()
//                .statusCode(HttpStatus.OK.value())
//                .message("Get Workshop Schedule")
//                .data(getScheduleDTO)
//                .build(), HttpStatus.OK);
//    }


    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<ResponseDTO> deleteSchdule (@PathVariable UUID id){
        workshopScheduleService.deleteSchedule(id, null);
        return new ResponseEntity<>(ResponseDTO.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Schedule Deleted")
                .data(null)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/schedules/multi-del")
    public ResponseEntity<ResponseDTO> deleteProducts(@RequestBody List<UUID> ids) {
        ResponseDTO responseDTO = workshopScheduleService.deleteMultiSchedule(ids);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}/open-time")
    public ResponseEntity<ResponseDTO<EditOpenTimeDTO>> changeTime(@PathVariable Integer id, @RequestBody EditOpenTimeDTO editOpenTimeDTO){
        workshopScheduleService.changeOpenTime(editOpenTimeDTO, id);
        return new ResponseEntity<>(ResponseDTO.<EditOpenTimeDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Time Updated")
                .data(editOpenTimeDTO)
                .build(), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/close-days")
    public ResponseEntity<ResponseDTO<EditCloseDayDTO>> changeCloseDay(@PathVariable Integer id, @RequestBody EditCloseDayDTO editCloseDayDTO){
        workshopScheduleService.changeCloseDay(editCloseDayDTO, id);
        return new ResponseEntity<>(ResponseDTO.<EditCloseDayDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Day Updated")
                .data(editCloseDayDTO)
                .build(), HttpStatus.CREATED);
    }


//    @PostMapping("/{id}/schedules/weekly")
//    public ResponseEntity<ResponseDTO<Object>> addScheduleWeekly(@PathVariable Integer id,
//                                                                 @RequestBody List<ScheduleDTO> schedules) {
//        List<ScheduleDTO> addedSchedules = workshopScheduleService.addScheduleWeekly(id, schedules);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ResponseDTO.builder()
//                        .statusCode(HttpStatus.CREATED.value())
//                        .message("Schedules added successfully")
//                        .data(addedSchedules)
//                        .build());
//    }

//    @PostMapping("/{workshopId}/schedules/monthly")
//    public ResponseEntity<ResponseDTO<List<ScheduleDTO>>> addScheduleMonthly(
//            @PathVariable("workshopId") Integer workshopId,
//            @RequestBody List<ScheduleDTO> scheduleDTOs) {
//
//        List<ScheduleDTO> addedSchedules = workshopScheduleService.addScheduleMonthly(workshopId, scheduleDTOs);
//
//        ResponseDTO<List<ScheduleDTO>> response = ResponseDTO.<List<ScheduleDTO>>builder()
//                .statusCode(HttpStatus.CREATED.value())
//                .message("Success")
//                .data(addedSchedules)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }

}
