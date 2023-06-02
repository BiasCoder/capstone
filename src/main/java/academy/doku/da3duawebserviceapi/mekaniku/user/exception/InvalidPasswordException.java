package academy.doku.da3duawebserviceapi.mekaniku.user.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvalidPasswordException extends RuntimeException {
    public String message;
}
