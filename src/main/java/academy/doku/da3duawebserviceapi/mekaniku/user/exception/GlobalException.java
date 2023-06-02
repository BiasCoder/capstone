package academy.doku.da3duawebserviceapi.mekaniku.user.exception;


import academy.doku.da3duawebserviceapi.mekaniku.user.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseDTO<Object>> globalException(Exception e) {
        return new ResponseEntity<>(ResponseDTO.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Bad Request")
                .data(e.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidPasswordException.class)
    public ResponseEntity<ResponseDTO<Object>> invalidPasswordException(Exception e) {
        return new ResponseEntity<>(ResponseDTO.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Password is invalid!")
                .data(e.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }


}
