package academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.controller;

import academy.doku.da3duawebserviceapi.common.WebMvcSecurityConfig;
import academy.doku.da3duawebserviceapi.common.dto.BaseResponse;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.dto.PaymentMethodResponse;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.service.impl.PaymentMethodServiceImpl;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = GetAllPaymentMethodController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(WebMvcSecurityConfig.class)
class GetAllPaymentMethodControllerTest {

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

    private String requestTarget = "/api/payment-methods";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private List<PaymentMethodResponse> generatePaymentMethodResponse() {
        LocalDateTime createdDate = LocalDateTime.of(2023, Month.MARCH, 13, 12, 30);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.MARCH, 13, 12, 30);

        return new ArrayList<>(
                Arrays.asList(
                        new PaymentMethodResponse(1, "VIRTUAL_ACCOUNT_BCA", "BCA", "VIRTUAL_ACCOUNT", true, "photo1.jpg", createdDate, modifiedDate),
                        new PaymentMethodResponse(2, "VIRTUAL_ACCOUNT_BRI", "BRI", "VIRTUAL_ACCOUNT", true, "photo2.jpg", createdDate, modifiedDate),
                        new PaymentMethodResponse(3, "VIRTUAL_ACCOUNT_BNI", "BNI", "VIRTUAL_ACCOUNT", true, "photo3.jpg", createdDate, modifiedDate)
                )
        );
    }

    @Test
    @DisplayName("When payment method endpoint is hit, should return response with status code 200")
    void get_payment_method_list() throws Exception {
        List<PaymentMethodResponse> paymentMethods = generatePaymentMethodResponse();
        BaseResponse response = BaseResponse.builder()
                .statusCode(200)
                .message("Payment Methods List")
                .data(paymentMethods)
                .build();

        when(securityContext.getAuthentication()).thenReturn(auth);

        when(auth.getPrincipal())
                .thenReturn(jwt);

        when(jwt.getClaim("role"))
                .thenReturn("SUPERADMIN");

        when(paymentMethodService.getAllPaymentMethods()).thenReturn(paymentMethods);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(MockMvcRequestBuilders.get(requestTarget))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}