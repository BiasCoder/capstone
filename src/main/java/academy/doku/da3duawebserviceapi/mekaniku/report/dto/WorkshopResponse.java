package academy.doku.da3duawebserviceapi.mekaniku.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id"})
public class WorkshopResponse implements Serializable  {

    @JsonProperty(value = "workshopId")
    private Integer id;

    private String name;
}
