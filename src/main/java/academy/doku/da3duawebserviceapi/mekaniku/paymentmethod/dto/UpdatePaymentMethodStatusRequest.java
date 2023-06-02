package academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaymentMethodStatusRequest implements Serializable {

    @JsonProperty(required = true)
    private Boolean active;

}
