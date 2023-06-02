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
public class MerchantOnlyException extends RuntimeException {
    public MerchantOnlyException(String message) {
        super(message);
        this.message = message;
    }

    public String message = "Only merchant can access this feature";
}
