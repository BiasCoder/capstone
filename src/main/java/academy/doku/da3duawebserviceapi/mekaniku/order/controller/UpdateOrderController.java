package academy.doku.da3duawebserviceapi.mekaniku.order.controller;

import academy.doku.da3duawebserviceapi.common.dto.BaseResponse;
import academy.doku.da3duawebserviceapi.common.utils.CheckRoleUtil;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.OrderResponse;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.UpdateOrderRequest;
import academy.doku.da3duawebserviceapi.mekaniku.order.service.UpdateOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@CrossOrigin
public class UpdateOrderController {

    private final UpdateOrderService updateOrderService;

    @CrossOrigin(origins = "*",allowedHeaders = "*")
    @PutMapping("/orders/{orderId}")
    public ResponseEntity<BaseResponse<OrderResponse>> updateOrder(
            @PathVariable UUID orderId,
            @RequestBody UpdateOrderRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkMerchantOnlyAccess(authentication);

        return ResponseEntity.ok(BaseResponse.<OrderResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Order with ID: " + orderId + " is updated")
                .data(updateOrderService.updateOrder(orderId, request))
                .build());
    }
}
