package academy.doku.da3duawebserviceapi.mekaniku.order.dto;

import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse implements Serializable {
    private UUID id;
    private String name;
    private Double price;
    private ProductType type;
    private Integer quantity;
}
