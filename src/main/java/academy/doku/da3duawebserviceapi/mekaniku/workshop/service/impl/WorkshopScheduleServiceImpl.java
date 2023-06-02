package academy.doku.da3duawebserviceapi.mekaniku.workshop.service.impl;


import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.*;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.ScheduleStatus;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopScheduleEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.repository.WorkshopRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.repository.WorkshopScheduleRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.WorkshopScheduleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkshopScheduleServiceImpl implements WorkshopScheduleService {

    private final WorkshopScheduleRepository workshopScheduleRepository;
    private final WorkshopRepository workshopRepository;

    private final ModelMapper modelMapper;

    private AddScheduleResponseDTO buildAddScheduleResponse(WorkshopScheduleEntity schedule) {
        AddScheduleResponseDTO response = new AddScheduleResponseDTO();
        response.setId(schedule.getId());
        response.setForDate(schedule.getForDate());
        response.setStatus(schedule.getStatus());
        response.setOpenTime(schedule.getOpenTime());
        response.setCloseTime(schedule.getCloseTime());
        return response;
    }


    @Override
    public AddScheduleResponseDTO addScheduleOnce(Integer workshopId, ScheduleDTO scheduleDTO) {
        WorkshopEntity workshop = workshopRepository.findById(workshopId)
                .orElseThrow();//(() -> new ResourceNotFoundException("Workshop", "id", workshopId));

        WorkshopScheduleEntity newSchedule = new WorkshopScheduleEntity();
        newSchedule.setWorkshop(workshop);
        newSchedule.setForDate(scheduleDTO.getForDate());
        newSchedule.setStatus(scheduleDTO.getStatus());
        if (scheduleDTO.getStatus() == ScheduleStatus.CLOSE) {
            newSchedule.setOpenTime(null);
            newSchedule.setCloseTime(null);
        } else {
            newSchedule.setOpenTime(scheduleDTO.getOpenTime());
            newSchedule.setCloseTime(scheduleDTO.getCloseTime());
        }

        WorkshopScheduleEntity savedSchedule = workshopScheduleRepository.save(newSchedule);

        return buildAddScheduleResponse(savedSchedule);
    }

    @Override
    public GetScheduleDTO getSchedule(Integer id) {
        Optional<WorkshopEntity> workshop = workshopRepository.findById(id);
        if (!workshop.isPresent()){
            throw new RuntimeException("Not found");
        }
        GetScheduleDTO getScheduleDTO = modelMapper.map(workshop.get(), GetScheduleDTO.class);
        List<WorkshopScheduleEntity> schedules = workshopScheduleRepository.findAllByWorkshopId(id);
        List<ScheduleDTO> List = schedules.stream()
                .map(schedule -> modelMapper.map(schedule, ScheduleDTO.class))
                .collect(Collectors.toList());
        getScheduleDTO.setList(List);
        return getScheduleDTO;
    }

    @Override
    public ResponseDTO deleteSchedule(UUID id, ResponseDTO responseDTO) {
        workshopScheduleRepository.deleteById(id);
        return responseDTO;
    }

    @Override
    public ResponseDTO deleteMultiSchedule(List<UUID> ids) {
        workshopScheduleRepository.deleteAllById(ids);
        return ResponseDTO.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Schedule Deleted")
                .data(ids)
                .build();
    }

    @Override
    public EditOpenTimeDTO changeOpenTime(EditOpenTimeDTO editOpenTimeDTO, Integer id) {
        Optional<WorkshopEntity> optionalWorkshopEntity = workshopRepository.findById(id);
        if (!optionalWorkshopEntity.isPresent()){
            throw new RuntimeException("Not Found");
        }

        WorkshopEntity workshop = optionalWorkshopEntity.get();
        workshop.setOpenTime(editOpenTimeDTO.getOpenTime());
        workshop.setCloseTime(editOpenTimeDTO.getCloseTime());
        workshopRepository.save(workshop);
        return editOpenTimeDTO;
    }

    @Override
    public EditCloseDayDTO changeCloseDay(EditCloseDayDTO editCloseDayDTO, Integer id) {
        Optional<WorkshopEntity> optionalWorkshopEntity = workshopRepository.findById(id);
        if (!optionalWorkshopEntity.isPresent()){
            throw new RuntimeException("Not Found");
        }

        WorkshopEntity workshop = optionalWorkshopEntity.get();
        workshop.setCloseDay(editCloseDayDTO.getCloseDay());
        workshopRepository.save(workshop);
        return editCloseDayDTO;
    }


//    private ScheduleDTO convertToScheduleDTO(WorkshopScheduleEntity workshopSchedule) {
//        ScheduleDTO scheduleDTO = new ScheduleDTO();
//        scheduleDTO.setId(workshopSchedule.getId());
//        scheduleDTO.setForDate(workshopSchedule.getForDate());
//        scheduleDTO.setStatus(workshopSchedule.getStatus());
//        scheduleDTO.setOpenTime(workshopSchedule.getOpenTime());
//        scheduleDTO.setCloseTime(workshopSchedule.getCloseTime());
//        return scheduleDTO;
//    }


//    @Override
//    public List<ScheduleDTO> addScheduleWeekly(Integer workshopId, List<ScheduleDTO> schedules) {
//        WorkshopEntity workshopEntity = workshopRepository.findById(workshopId)
//                .orElseThrow();
//        List<WorkshopScheduleEntity> scheduleEntities = new ArrayList<>();
//
//        for (ScheduleDTO scheduleDTO : schedules) {
//            WorkshopScheduleEntity scheduleEntity = new WorkshopScheduleEntity();
//            scheduleEntity.setWorkshop(workshopEntity);
//            scheduleEntity.setForDate(scheduleDTO.getForDate());
//            scheduleEntity.setStatus(scheduleDTO.getStatus());
//
//            if (scheduleDTO.getStatus() == ScheduleStatus.CLOSE) {
//                scheduleEntity.setOpenTime(null);
//                scheduleEntity.setCloseTime(null);
//            } else {
//                scheduleEntity.setOpenTime(scheduleDTO.getOpenTime());
//                scheduleEntity.setCloseTime(scheduleDTO.getCloseTime());
//            }
//
//            scheduleEntities.add(scheduleEntity);
//        }
//
//        scheduleEntities = workshopScheduleRepository.saveAll(scheduleEntities);
//
//
//
//        return scheduleEntities.stream()
//                .map(this::convertToScheduleDTO)
//                .collect(Collectors.toList());
//    }

//    @Override
//    public List<ScheduleDTO> addScheduleMonthly(Integer workshopId, List<ScheduleDTO> scheduleDTOs) {
//        List<ScheduleDTO> addedSchedules = new ArrayList<>();
//        WorkshopEntity workshop = workshopRepository.findById(workshopId)
//                .orElseThrow();//(() -> new ResourceNotFoundException("Workshop", "id", workshopId));
//
//        for (ScheduleDTO scheduleDTO : scheduleDTOs) {
//            LocalDate startDate = scheduleDTO.getForDate();
//            LocalDate endDate = startDate.plusMonths(1).minusDays(1); // get the last day of the month
//            List<LocalDate> scheduleDates = getDatesBetween(startDate, endDate);
//
//            for (LocalDate date : scheduleDates) {
//                ScheduleStatus status = scheduleDTO.getStatus();
//                LocalTime openTime = status == ScheduleStatus.CLOSE ? null : scheduleDTO.getOpenTime();
//                LocalTime closeTime = status == ScheduleStatus.CLOSE ? null : scheduleDTO.getCloseTime();
//
//                WorkshopScheduleEntity schedule = WorkshopScheduleEntity.builder()
//                        .forDate(date)
//                        .status(status)
//                        .openTime(openTime)
//                        .closeTime(closeTime)
//                        .workshop(workshop)
//                        .build();
//
//                WorkshopScheduleEntity addedSchedule = workshopScheduleRepository.save(schedule);
//                addedSchedules.add(convertToDto(addedSchedule));
//            }
//        }
//
//        return addedSchedules;
//    }

//    private List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
//        List<LocalDate> dates = new ArrayList<>();
//        LocalDate currentDate = startDate;
//
//        while (!currentDate.isAfter(endDate)) {
//            dates.add(currentDate);
//            currentDate = currentDate.plusDays(1);
//        }
//        return dates;
//    }

//    private ScheduleDTO convertToDto(WorkshopScheduleEntity scheduleEntity) {
//        ScheduleDTO scheduleDTO = new ScheduleDTO();
//        BeanUtils.copyProperties(scheduleEntity, scheduleDTO);
//        return scheduleDTO;
//    }


}
