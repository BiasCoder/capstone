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
public class WorkshopDTO implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private String address;
    private Double latitude;
    private Double longitude;
    private Boolean motorcycle;
    private Boolean car;
    private LocalTime openTime;
    private LocalTime closeTime;
    private List<Integer> closeDay;
    private UserContactDTO user;
    private List<WorkshopPhotoResponse> workshopPhotos;

}
