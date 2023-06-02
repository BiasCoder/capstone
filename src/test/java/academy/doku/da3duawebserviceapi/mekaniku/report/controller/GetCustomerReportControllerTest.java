package academy.doku.da3duawebserviceapi.mekaniku.report.controller;

import academy.doku.da3duawebserviceapi.common.dto.BaseResponse;
import academy.doku.da3duawebserviceapi.common.dto.PaginationResponse;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.CustomerResponse;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.enums.OrderStatus;
import academy.doku.da3duawebserviceapi.mekaniku.report.dto.ReportResponse;
import academy.doku.da3duawebserviceapi.mekaniku.report.dto.WorkshopResponse;
import academy.doku.da3duawebserviceapi.mekaniku.report.service.impl.TransactionReportServiceImpl;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = GetCustomerReportController.class)
@AutoConfigureMockMvc(addFilters = false)
class GetCustomerReportControllerTest {
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

    @MockBean
    UserSuspendClaimAdapter userSuspendClaimAdapter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private PaginationResponse<List<ReportResponse>> generateResponse() {

        UUID report1Id = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75");
        UUID report2Id = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d76");

        CustomerResponse customerResponse = new CustomerResponse("John Doe", "087888682331", "john.doe@gmail.com");
        WorkshopResponse workshopResponse = new WorkshopResponse(1, "AHASS Cibubur");


        List<ReportResponse> reports = new ArrayList<>();

        reports.add(new ReportResponse(report1Id, "ORDER-21312", null, null, workshopResponse, customerResponse, "CAR", "B 1234 AB", OrderStatus.DONE, null, LocalDateTime.of(2023, 3, 20, 12, 30), null, "45000"));
        reports.add(new ReportResponse(report2Id, "ORDER-12345", null, null, workshopResponse, customerResponse, "CAR", "B 1234 AB", OrderStatus.CANCELED, LocalDateTime.of(2023, 3, 20, 12, 30), null, null, "45000"));

        return new PaginationResponse<>(reports, 2L, 1, 1);
    }

    @Test
    @DisplayName("When get customer report endpoint is hit, should return response with status code 200")
    void get_customer_report() throws Exception {
        Integer customerId = 1;

        Pageable pageable = PageRequest.of(0, 10);

        PaginationResponse<List<ReportResponse>> customerReport = generateResponse();
        BaseResponse response = BaseResponse.builder()
                .statusCode(200)
                .message("Report list for Customer with ID:" + customerId)
                .data(customerReport)
                .build();

        when(securityContext.getAuthentication()).thenReturn(auth);

        when(auth.getPrincipal())
                .thenReturn(jwt);

        when(jwt.getClaim("role"))
                .thenReturn("SUPERADMIN");

        when(transactionReportService.getCustomerReport(pageable, customerId)).thenReturn(customerReport);
        SecurityContextHolder.setContext(securityContext);

        String requestTarget = "/api/customers/" + customerId + "/reports";
        mockMvc.perform(MockMvcRequestBuilders.get(requestTarget)
                        .param("page", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}