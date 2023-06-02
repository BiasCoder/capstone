package academy.doku.da3duawebserviceapi.mekaniku.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusRequest {
    @JsonProperty(required = true)
    private String status;

    private String rejectionNote;
}
