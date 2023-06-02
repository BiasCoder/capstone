package academy.doku.da3duawebserviceapi.mekaniku.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequest implements Serializable {

    private List<UpdateOrderDetailRequest> products;

    private String workshopNote;
}
