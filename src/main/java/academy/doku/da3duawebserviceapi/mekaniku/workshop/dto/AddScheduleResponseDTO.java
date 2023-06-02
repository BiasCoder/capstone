package academy.doku.da3duawebserviceapi.mekaniku.workshop.dto;


import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.ScheduleStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class AddScheduleResponseDTO {
    private UUID id;
    private LocalDate forDate;
    private ScheduleStatus status;
    private LocalTime openTime;
    private LocalTime closeTime;
}
