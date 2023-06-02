package academy.doku.da3duawebserviceapi.mekaniku.order.exception;

import academy.doku.da3duawebserviceapi.common.ExceptionHandlerAdvisor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionHandlerAdvisor extends ExceptionHandlerAdvisor {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Object> handlerOrderNotFound(OrderNotFoundException e) {
        return new ResponseEntity<>(
                buildResponse(
                        HttpStatus.NOT_FOUND.value(),
                        "INVALID_ID",
                        e.getMessage()
                ),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidStatusParamException.class)
    public ResponseEntity<Object> handlerInvalidStatus(InvalidStatusParamException e) {
        return new ResponseEntity<>(
                buildResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "INVALID_STATUS",
                        e.getMessage()
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RejectionNoteEmptyException.class)
    public ResponseEntity<Object> handlerRejectionNoteEmpty(RejectionNoteEmptyException e) {
        return new ResponseEntity<>(
                buildResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "BAD_REQUEST",
                        e.getMessage()
                ),
                HttpStatus.BAD_REQUEST);
    }
}
