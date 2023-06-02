package academy.doku.da3duawebserviceapi.mekaniku.workshop.dto;

import lombok.*;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class EditOpenTimeDTO {
    LocalTime openTime;
    LocalTime closeTime;
}
