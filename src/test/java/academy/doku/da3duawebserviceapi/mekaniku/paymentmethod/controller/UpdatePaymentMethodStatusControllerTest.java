package academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.controller;

import academy.doku.da3duawebserviceapi.common.WebMvcSecurityConfig;
import academy.doku.da3duawebserviceapi.common.dto.BaseResponse;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.dto.PaymentMethodResponse;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.dto.UpdatePaymentMethodStatusRequest;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.service.impl.PaymentMethodServiceImpl;
import academy.doku.da3duawebserviceapi.mekaniku.user.config.UserSuspendClaimAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UpdatePaymentMethodStatusController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(WebMvcSecurityConfig.class)
class UpdatePaymentMethodStatusControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PaymentMethodServiceImpl paymentMethodService;

    @Mock
    Authentication auth;

    @Mock
    SecurityContext securityContext;

    @Mock
    Jwt jwt;

    private String requestTarget = "/api/payment-methods/";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    private UpdatePaymentMethodStatusRequest generateRequest() {
        return new UpdatePaymentMethodStatusRequest(false);
    }
    private PaymentMethodResponse generatePaymentMethodResponse() {
        LocalDateTime createdDate = LocalDateTime.of(2023, Month.MARCH, 13, 12, 30);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.MARCH, 13, 12, 30);

        return new PaymentMethodResponse(1, "VIRTUAL_ACCOUNT_BCA", "BCA", "VIRTUAL_ACCOUNT", false, "photo1.jpg", createdDate, modifiedDate);
    }

    @Test
    @DisplayName("When update status endpoint is hit, should return response with status code 200")
    void update_payment_method_status() throws Exception {

        Integer paymentMethodId = 1;

        PaymentMethodResponse paymentMethod = generatePaymentMethodResponse();

        UpdatePaymentMethodStatusRequest request = generateRequest();
        BaseResponse response = BaseResponse.builder()
                .statusCode(200)
                .message("Payment method updated")
                .data(paymentMethod)
                .build();

        when(securityContext.getAuthentication())
                .thenReturn(auth);

        when(auth.getPrincipal())
                .thenReturn(jwt);

        when(jwt.getClaim("role"))
                .thenReturn("SUPERADMIN");

        when(paymentMethodService.updateState(paymentMethodId, request.getActive())).thenReturn(paymentMethod);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(patch(requestTarget + paymentMethodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}