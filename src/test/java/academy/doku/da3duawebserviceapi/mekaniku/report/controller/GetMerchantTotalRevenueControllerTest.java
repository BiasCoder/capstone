package academy.doku.da3duawebserviceapi.mekaniku.report.controller;

import academy.doku.da3duawebserviceapi.common.WebMvcSecurityConfig;
import academy.doku.da3duawebserviceapi.common.dto.BaseResponse;
import academy.doku.da3duawebserviceapi.mekaniku.report.service.impl.TransactionReportServiceImpl;
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

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GetMerchantTotalRevenueController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(WebMvcSecurityConfig.class)
class GetMerchantTotalRevenueControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TransactionReportServiceImpl transactionReportService;

    @Mock
    Authentication auth;

    @Mock
    SecurityContext securityContext;

    @Mock
    Jwt jwt;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("When get customer total spent endpoint is hit, should return response with status code 200")
    void get_customer_total_spent() throws Exception {
        Integer workshopId = 1;

        Double totalRevenues = 12450000.0;
        BaseResponse response = BaseResponse.builder()
                .statusCode(200)
                .message("Total revenues for Workshop with ID:" + workshopId)
                .data(BigDecimal.valueOf(totalRevenues).toPlainString() )
                .build();

        when(securityContext.getAuthentication()).thenReturn(auth);

        when(auth.getPrincipal())
                .thenReturn(jwt);

        when(jwt.getClaim("role"))
                .thenReturn("SUPERADMIN");

        when(transactionReportService.getRevenue(workshopId)).thenReturn(totalRevenues);
        SecurityContextHolder.setContext(securityContext);

        String requestTarget = "/api/workshops/" + workshopId + "/revenues";
        mockMvc.perform(MockMvcRequestBuilders.get(requestTarget))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}