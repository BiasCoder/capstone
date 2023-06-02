package academy.doku.da3duawebserviceapi.mekaniku.workshop.exception;

import academy.doku.da3duawebserviceapi.common.ExceptionHandlerAdvisor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class WorkshopExceptionHandlerAdvisor extends ExceptionHandlerAdvisor {

    @ExceptionHandler(UploadPhotoFailedException.class)
    public ResponseEntity<Object> handlerStorageUploadFailed(UploadPhotoFailedException e) {
        return new ResponseEntity<>(
                buildResponse(
                        HttpStatus.UNPROCESSABLE_ENTITY.value(),
                        "STORAGE_FAILED",
                        e.message
                ),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(DeletePhotoFailedException.class)
    public ResponseEntity<Object> handlerStorageDeleteFailed(DeletePhotoFailedException e) {
        return new ResponseEntity<>(
                buildResponse(
                        HttpStatus.UNPROCESSABLE_ENTITY.value(),
                        "STORAGE_FAILED",
                        e.message
                ),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
