package academy.doku.da3duawebserviceapi.mekaniku.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderDetailRequest implements Serializable {

    private UUID productId;

    private Integer quantity;
}
