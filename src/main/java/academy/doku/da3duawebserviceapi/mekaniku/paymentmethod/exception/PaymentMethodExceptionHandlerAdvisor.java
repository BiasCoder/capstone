package academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.exception;

import academy.doku.da3duawebserviceapi.common.ExceptionHandlerAdvisor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PaymentMethodExceptionHandlerAdvisor extends ExceptionHandlerAdvisor {

    @ExceptionHandler(PaymentMethodNotFoundException.class)
    public ResponseEntity<Object> handlerPaymentMethodNotFound(PaymentMethodNotFoundException e) {
        return new ResponseEntity<>(
                buildResponse(
                        HttpStatus.NOT_FOUND.value(),
                        "INVALID_PAYMENT_ID",
                        e.getMessage()
                ),
                HttpStatus.NOT_FOUND);
    }
}