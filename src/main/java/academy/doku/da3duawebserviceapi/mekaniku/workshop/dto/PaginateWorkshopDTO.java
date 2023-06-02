package academy.doku.da3duawebserviceapi.mekaniku.workshop.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaginateWorkshopDTO implements Serializable {
    private List<WorkshopDetailDTO> List;
    private Long totalItems;
    private Long currentPage;
    private Long totalPage;

}
