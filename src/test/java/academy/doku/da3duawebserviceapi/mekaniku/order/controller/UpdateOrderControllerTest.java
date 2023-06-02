package academy.doku.da3duawebserviceapi.mekaniku.order.controller;

import academy.doku.da3duawebserviceapi.common.WebMvcSecurityConfig;
import academy.doku.da3duawebserviceapi.common.dto.BaseResponse;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.*;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.enums.OrderStatus;
import academy.doku.da3duawebserviceapi.mekaniku.order.service.impl.OrderServiceImpl;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.ProductType;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UpdateOrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(WebMvcSecurityConfig.class)
class UpdateOrderControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderServiceImpl orderService;

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

    private OrderResponse generateResponse() {
        LocalDateTime bookedTime = LocalDateTime.of(2023, Month.MARCH, 13, 12, 30);

        UUID orderId = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75");
        UUID orderDetail1Id = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d88");
        UUID orderDetail2Id = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d99");

        CustomerResponse customerResponse = new CustomerResponse("John Doe", "087888682331", "john.doe@gmail.com");

        List<OrderDetailResponse> orderDetails = new ArrayList<>();
        orderDetails.add(new OrderDetailResponse(orderDetail1Id, "Ganti Oli", 15000.0, ProductType.SERVICE, 1));
        orderDetails.add(new OrderDetailResponse(orderDetail2Id, "Oli Castrol", 45000.0, ProductType.GOODS, 1));

        return new OrderResponse(orderId, "ORDER-21312", customerResponse, null, null, LocalDate.of(2023, 3, 19), LocalTime.of(8, 30), "MOTORCYCLE", "B 1234 ABA", null, null, null, OrderStatus.ACCEPTED, bookedTime, null, bookedTime.plusMinutes(5), null, null, null, null, orderDetails, "60000");
    }

    private UpdateOrderRequest generateRequest() {
        UUID product1Id = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d88");
        UUID product2Id = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d99");

        List<UpdateOrderDetailRequest> products = new ArrayList<>();
        products.add(new UpdateOrderDetailRequest(product1Id, 1));
        products.add(new UpdateOrderDetailRequest(product2Id, 1));

        return new UpdateOrderRequest(products, "Harus ganti oli mesin");
    }

    @Test
    @DisplayName("When get merchant order endpoint is hit, should return response with status code 200")
    void update_order_status() throws Exception {
        UUID orderId = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75");


        UpdateOrderRequest request = generateRequest();
        OrderResponse UpdatedOrder = generateResponse();
        BaseResponse<Object> response = BaseResponse.builder()
                .statusCode(200)
                .message("Order with ID: " + orderId + " is updated")
                .data(UpdatedOrder)
                .build();

        when(securityContext.getAuthentication()).thenReturn(auth);

        when(auth.getPrincipal())
                .thenReturn(jwt);

        when(jwt.getClaim("role"))
                .thenReturn("WORKSHOP");

        when(orderService.updateOrder(orderId, request)).thenReturn(UpdatedOrder);
        SecurityContextHolder.setContext(securityContext);

        String requestTarget = "/api/orders/" + orderId;
        mockMvc.perform(MockMvcRequestBuilders.put(requestTarget)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}