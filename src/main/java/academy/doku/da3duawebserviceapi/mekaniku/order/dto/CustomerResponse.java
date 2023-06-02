package academy.doku.da3duawebserviceapi.mekaniku.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"fullName"})
public class CustomerResponse implements Serializable {

    @JsonProperty(value = "name")
    private String fullName;

    private String phone;

    private String email;
}
