package academy.doku.da3duawebserviceapi.mekaniku.report.dto;

import academy.doku.da3duawebserviceapi.mekaniku.order.dto.CustomerResponse;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse implements Serializable {
    private UUID id;

    private String orderNumber;
    private String invoiceNumber;
    private PaymentReportResponse invoice;

    private WorkshopResponse workshop;
    private CustomerResponse customer;

    private String vehicleType;
    private String vehicleNumber;

    private OrderStatus status;

    private LocalDateTime canceledAt;
    private LocalDateTime doneAt;
    private LocalDateTime rejectedAt;

    private String totalPrice;
}
