package academy.doku.da3duawebserviceapi.common.exception.role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Forbidden Access")
public class CustomerForbiddenException extends RuntimeException {

    public CustomerForbiddenException(String message) {
        super(message);
        this.message = message;
    }

    public String message = "Customer can't access this resource";
}
