package academy.doku.da3duawebserviceapi.common;

import academy.doku.da3duawebserviceapi.common.exception.role.CustomerForbiddenException;
import academy.doku.da3duawebserviceapi.common.exception.role.MerchantOnlyException;
import academy.doku.da3duawebserviceapi.mekaniku.order.exception.InvalidStatusParamException;
import academy.doku.da3duawebserviceapi.mekaniku.order.exception.OrderExceptionHandlerAdvisor;
import academy.doku.da3duawebserviceapi.mekaniku.order.exception.OrderNotFoundException;
import academy.doku.da3duawebserviceapi.mekaniku.order.exception.RejectionNoteEmptyException;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.exception.PaymentMethodExceptionHandlerAdvisor;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.exception.PaymentMethodNotFoundException;
import academy.doku.da3duawebserviceapi.common.exception.role.SuperAdminOnlyException;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlerAdvisorTest {

    ExceptionHandlerAdvisor exceptionHandler = new ExceptionHandlerAdvisor();
    PaymentMethodExceptionHandlerAdvisor paymentMethodExceptionHandler = new PaymentMethodExceptionHandlerAdvisor();

    OrderExceptionHandlerAdvisor orderExceptionHandlerAdvisor = new OrderExceptionHandlerAdvisor();

    @Test
    void handleNullRequestRelation() {
        String message = "Please make sure all request is not empty or null";
        String entityName = "PaymentMethodEntity";
        String propertyName = "active";

        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
        expectedResponse.put("error", "BAD_REQUEST");
        expectedResponse.put("message", message);

        ResponseEntity<Object> response = exceptionHandler.handlerNullRequestRelation(new PropertyValueException(message, entityName, propertyName));

        assertEquals(expectedResponse, response.getBody());
        assertEquals(expectedResponse.get("statusCode"), response.getStatusCodeValue());

    }

    // Role Exception

    @Test
    void handle_super_admin_only_default() {
        String message = "Only superadmin can access this feature";
        String expectedReason = "Forbidden";

        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("statusCode", HttpStatus.FORBIDDEN.value());
        expectedResponse.put("error", "INVALID_TOKEN");
        expectedResponse.put("message", message);

        ResponseEntity<Object> response = exceptionHandler.handlerSuperadminOnly(new SuperAdminOnlyException());

        assertEquals(expectedResponse, response.getBody());
        assertEquals(expectedResponse.get("statusCode"), response.getStatusCodeValue());
        assertEquals(expectedReason, response.getStatusCode().getReasonPhrase());
    }

    @Test
    void handle_super_admin_only_custom() {
        String message = "Only superadmin user can access this feature";
        String expectedReason = "Forbidden";

        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("statusCode", HttpStatus.FORBIDDEN.value());
        expectedResponse.put("error", "INVALID_TOKEN");
        expectedResponse.put("message", message);

        ResponseEntity<Object> response = exceptionHandler.handlerSuperadminOnly(new SuperAdminOnlyException(message));

        assertEquals(expectedResponse, response.getBody());
        assertEquals(expectedResponse.get("statusCode"), response.getStatusCodeValue());
        assertEquals(expectedReason, response.getStatusCode().getReasonPhrase());
    }

    @Test
    void handle_merchant_only_default() {
        String message = "Only merchant can access this feature";
        String expectedReason = "Forbidden";

        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("statusCode", HttpStatus.FORBIDDEN.value());
        expectedResponse.put("error", "INVALID_TOKEN");
        expectedResponse.put("message", message);

        ResponseEntity<Object> response = exceptionHandler.handlerMerchantOnly(new MerchantOnlyException());

        assertEquals(expectedResponse, response.getBody());
        assertEquals(expectedResponse.get("statusCode"), response.getStatusCodeValue());
        assertEquals(expectedReason, response.getStatusCode().getReasonPhrase());
    }

    @Test
    void handle_merchant_only_custom() {
        String message = "Only merchant user can access this feature";
        String expectedReason = "Forbidden";

        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("statusCode", HttpStatus.FORBIDDEN.value());
        expectedResponse.put("error", "INVALID_TOKEN");
        expectedResponse.put("message", message);

        ResponseEntity<Object> response = exceptionHandler.handlerMerchantOnly(new MerchantOnlyException(message));

        assertEquals(expectedResponse, response.getBody());
        assertEquals(expectedResponse.get("statusCode"), response.getStatusCodeValue());
        assertEquals(expectedReason, response.getStatusCode().getReasonPhrase());
    }

    @Test
    void handle_customer_forbidden_default() {
        String message = "Customer can't access this resource";
        String expectedReason = "Forbidden";

        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("statusCode", HttpStatus.FORBIDDEN.value());
        expectedResponse.put("error", "INVALID_TOKEN");
        expectedResponse.put("message", message);

        ResponseEntity<Object> response = exceptionHandler.handlerCustomerCantAccess(new CustomerForbiddenException());

        assertEquals(expectedResponse, response.getBody());
        assertEquals(expectedResponse.get("statusCode"), response.getStatusCodeValue());
        assertEquals(expectedReason, response.getStatusCode().getReasonPhrase());
    }

    @Test
    void handle_customer_forbidden_custom() {
        String message = "Customer user can't access this resource";
        String expectedReason = "Forbidden";

        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("statusCode", HttpStatus.FORBIDDEN.value());
        expectedResponse.put("error", "INVALID_TOKEN");
        expectedResponse.put("message", message);

        ResponseEntity<Object> response = exceptionHandler.handlerCustomerCantAccess(new CustomerForbiddenException(message));

        assertEquals(expectedResponse, response.getBody());
        assertEquals(expectedResponse.get("statusCode"), response.getStatusCodeValue());
        assertEquals(expectedReason, response.getStatusCode().getReasonPhrase());
    }

    // Payment Method Exception
    @Test
    void handle_payment_method_not_found() {
        int paymentMethodId = 1;
        String message = String.format("Payment method with ID: %s is not found.", paymentMethodId);
        String expectedReason = "Not Found";

        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("statusCode", HttpStatus.NOT_FOUND.value());
        expectedResponse.put("error", "INVALID_PAYMENT_ID");
        expectedResponse.put("message", message);

        ResponseEntity<Object> response = paymentMethodExceptionHandler.handlerPaymentMethodNotFound(new PaymentMethodNotFoundException(message));

        assertEquals(expectedResponse, response.getBody());
        assertEquals(expectedResponse.get("statusCode"), response.getStatusCodeValue());
        assertEquals(expectedReason, response.getStatusCode().getReasonPhrase());
    }

    // Payment Method Exception
    @Test
    void handle_order_not_found() {
        UUID orderId = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75");
        String message = "Order with ID: " + orderId + " not found";
        String expectedReason = "Not Found";

        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("statusCode", HttpStatus.NOT_FOUND.value());
        expectedResponse.put("error", "INVALID_ID");
        expectedResponse.put("message", message);

        ResponseEntity<Object> response = orderExceptionHandlerAdvisor.handlerOrderNotFound(new OrderNotFoundException(message));

        assertEquals(expectedResponse, response.getBody());
        assertEquals(expectedResponse.get("statusCode"), response.getStatusCodeValue());
        assertEquals(expectedReason, response.getStatusCode().getReasonPhrase());
    }

    @Test
    void handle_invalid_status_param() {
        String message = "Status is invalid";
        String expectedReason = "Bad Request";

        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
        expectedResponse.put("error", "INVALID_STATUS");
        expectedResponse.put("message", message);

        ResponseEntity<Object> response = orderExceptionHandlerAdvisor.handlerInvalidStatus(new InvalidStatusParamException(message));

        assertEquals(expectedResponse, response.getBody());
        assertEquals(expectedResponse.get("statusCode"), response.getStatusCodeValue());
        assertEquals(expectedReason, response.getStatusCode().getReasonPhrase());
    }

    @Test
    void handle_rejection_note_empty() {
        String message = "Rejection Note is empty";
        String expectedReason = "Bad Request";

        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
        expectedResponse.put("error", "BAD_REQUEST");
        expectedResponse.put("message", message);

        ResponseEntity<Object> response = orderExceptionHandlerAdvisor.handlerRejectionNoteEmpty(new RejectionNoteEmptyException(message));

        assertEquals(expectedResponse, response.getBody());
        assertEquals(expectedResponse.get("statusCode"), response.getStatusCodeValue());
        assertEquals(expectedReason, response.getStatusCode().getReasonPhrase());
    }
}