package academy.doku.da3duawebserviceapi.mekaniku.order.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad Request")
public class InvalidStatusParamException extends RuntimeException {
    public final String message;
}