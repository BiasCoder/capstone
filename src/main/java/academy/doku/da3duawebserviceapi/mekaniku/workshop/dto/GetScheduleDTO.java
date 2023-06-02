package academy.doku.da3duawebserviceapi.mekaniku.workshop.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class GetScheduleDTO implements Serializable {
    private Integer id;
    private LocalTime openTime;
    private LocalTime closeTime;
    private List<Integer> closeDay;
    private List<ScheduleDTO> List;
}
