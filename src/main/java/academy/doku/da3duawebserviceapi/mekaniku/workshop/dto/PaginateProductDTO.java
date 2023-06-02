package academy.doku.da3duawebserviceapi.mekaniku.workshop.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaginateProductDTO implements Serializable {

    private List<ProductDTO> List;
    private Long totalItems;
    private Integer currentPage;
    private Integer totalPage;

}
