package academy.doku.da3duawebserviceapi.common;

import academy.doku.da3duawebserviceapi.common.exception.role.CustomerForbiddenException;
import academy.doku.da3duawebserviceapi.common.exception.role.MerchantOnlyException;
import academy.doku.da3duawebserviceapi.common.exception.role.SuperAdminOnlyException;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class ExceptionHandlerAdvisor {

    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<Object> handlerNullRequestRelation(PropertyValueException e) {
        return new ResponseEntity<>(
                buildResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "BAD_REQUEST",
                        "Please make sure all request is not empty or null"
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handlerEmptyRequiredRequestBody(HttpMessageNotReadableException e) {
        String message = Objects.requireNonNull(e.getMessage()).split(":")[0];

        if (message.contains("JSON")) {
            message = "JSON parse error. Expecting boolean value";
        } else {
            message = "Required request body is missing";
        }

        return new ResponseEntity<>(
                buildResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "BAD_REQUEST",
                        message
                ),
                HttpStatus.BAD_REQUEST);
    }

    // ROLE EXCEPTION
    @ExceptionHandler(SuperAdminOnlyException.class)
    public ResponseEntity<Object> handlerSuperadminOnly(SuperAdminOnlyException e) {
        return new ResponseEntity<>(
                buildResponse(
                        HttpStatus.FORBIDDEN.value(),
                        "INVALID_TOKEN",
                        e.getMessage()
                ),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MerchantOnlyException.class)
    public ResponseEntity<Object> handlerMerchantOnly(MerchantOnlyException e) {
        return new ResponseEntity<>(
                buildResponse(
                        HttpStatus.FORBIDDEN.value(),
                        "INVALID_TOKEN",
                        e.getMessage()
                ),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CustomerForbiddenException.class)
    public ResponseEntity<Object> handlerCustomerCantAccess(CustomerForbiddenException e) {
        return new ResponseEntity<>(
                buildResponse(
                        HttpStatus.FORBIDDEN.value(),
                        "INVALID_TOKEN",
                        e.message
                ),
                HttpStatus.FORBIDDEN);
    }

    public Map<String, Object> buildResponse(int code, String error, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("statusCode", code);
        response.put("error", error);
        response.put("message", message);
        return response;
    }
}