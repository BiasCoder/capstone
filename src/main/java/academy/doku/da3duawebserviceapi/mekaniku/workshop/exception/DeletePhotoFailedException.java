package academy.doku.da3duawebserviceapi.mekaniku.workshop.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeletePhotoFailedException extends RuntimeException {
    public String message;
}
