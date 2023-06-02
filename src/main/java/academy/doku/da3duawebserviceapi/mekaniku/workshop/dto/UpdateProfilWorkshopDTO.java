package academy.doku.da3duawebserviceapi.mekaniku.workshop.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class UpdateProfilWorkshopDTO {
    String name;
    String phone;
    String address;
    Double latitude;
    Double longitude;
}
