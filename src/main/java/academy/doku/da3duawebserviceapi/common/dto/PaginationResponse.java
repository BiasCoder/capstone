package academy.doku.da3duawebserviceapi.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"totalItem", "currentPage", "totalPage"})
public class PaginationResponse<T> implements Serializable {
    private T list;
    private Long totalItem;
    private Integer currentPage;
    private Integer totalPage;
}

