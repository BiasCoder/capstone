package academy.doku.da3duawebserviceapi.mekaniku.report.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"date", "success"})
public class ReportDiagramResponse implements Serializable {
    private LocalDateTime date;

    private Integer success;
    private Integer rejected;
    private Integer canceled;
}
