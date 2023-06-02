package academy.doku.da3duawebserviceapi.mekaniku.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReportResponse implements Serializable {

    private String paymentName;
    private String paymentImageUrl;
}