package academy.doku.da3duawebserviceapi.mekaniku.order.dto;

import academy.doku.da3duawebserviceapi.mekaniku.order.entity.enums.OrderStatus;
import academy.doku.da3duawebserviceapi.mekaniku.report.dto.PaymentReportResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse implements Serializable {
    private UUID id;

    private String orderNumber;

    private CustomerResponse customer;

    private String invoiceNumber;
    private PaymentReportResponse invoice;

    private LocalDate bookForDate;

    private LocalTime bookForTime;

    private String vehicleType;

    private String vehicleNumber;

    private String customerNote;

    private String workshopNote;

    private String rejectionNote;

    private OrderStatus status;

    private LocalDateTime bookedAt;

    private LocalDateTime canceledAt;

    private LocalDateTime acceptedAt;

    private LocalDateTime inProcessAt;

    private LocalDateTime processedAt;

    private LocalDateTime doneAt;

    private LocalDateTime rejectedAt;

    @JsonProperty(value = "products")
    private List<OrderDetailResponse> orderDetails;

    private String totalPrice;

}
