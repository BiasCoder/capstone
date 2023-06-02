package academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodResponse implements Serializable {
    private Integer id;

    private String name;

    private String description;

    private String category;

    private Boolean active;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
