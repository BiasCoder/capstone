package academy.doku.da3duawebserviceapi.mekaniku.workshop.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class EditCloseDayDTO {
    private List<Integer> closeDay;
}
