package academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Resource Not Found")
public class PaymentMethodNotFoundException extends RuntimeException {
    public final String message;
}
