package academy.doku.da3duawebserviceapi.mekaniku.workshop.service;

import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.*;

import java.util.List;
import java.util.UUID;

public interface WorkshopScheduleService {
    AddScheduleResponseDTO addScheduleOnce(Integer workshopId, ScheduleDTO scheduleDTO);


    GetScheduleDTO getSchedule (Integer id);

    ResponseDTO deleteSchedule (UUID id, ResponseDTO responseDTO);

    ResponseDTO deleteMultiSchedule (List<UUID> ids);

    EditOpenTimeDTO changeOpenTime (EditOpenTimeDTO editOpenTimeDTO, Integer id);

    EditCloseDayDTO changeCloseDay (EditCloseDayDTO editCloseDayDTO, Integer id);

}
