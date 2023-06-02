package academy.doku.da3duawebserviceapi.mekaniku.workshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkshopPhotoResponse implements Serializable {
    private UUID id;
    private String photoUrl;
//    private LocalDateTime createdAt;
//    private LocalDateTime modifiedAt;
}
