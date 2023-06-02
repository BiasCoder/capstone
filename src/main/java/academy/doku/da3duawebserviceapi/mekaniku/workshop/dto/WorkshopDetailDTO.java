package academy.doku.da3duawebserviceapi.mekaniku.workshop.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class WorkshopDetailDTO {
    Integer id;
    String name;
    String address;
    String description;
    UserContactDTO user;
    List<WorkshopPhotoResponse> workshopPhotos;
}
