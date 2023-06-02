package academy.doku.da3duawebserviceapi.mekaniku.order.controller;

import academy.doku.da3duawebserviceapi.common.dto.BaseResponse;
import academy.doku.da3duawebserviceapi.common.utils.CheckRoleUtil;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.OrderResponse;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.OrderStatusRequest;
import academy.doku.da3duawebserviceapi.mekaniku.order.service.UpdateOrderStatusService;
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
public class UpdateOrderStatusController {

    private final UpdateOrderStatusService updateOrderStatusService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PatchMapping("/orders/{orderId}/status")
    public ResponseEntity<BaseResponse<OrderResponse>> updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestBody OrderStatusRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkMerchantOnlyAccess(authentication);

        return ResponseEntity.ok(BaseResponse.<OrderResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Order status updated to " + request.getStatus())
                .data(updateOrderStatusService.updateStatus(orderId, request))
                .build());
    }
}
