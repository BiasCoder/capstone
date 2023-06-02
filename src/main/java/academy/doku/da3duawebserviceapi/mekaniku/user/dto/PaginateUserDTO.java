package academy.doku.da3duawebserviceapi.mekaniku.user.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaginateUserDTO implements Serializable {
    private List<GetAllUserDTO> List;
    private Long totalItems;
    private Long currentPage;
    private Long totalPage;
}
