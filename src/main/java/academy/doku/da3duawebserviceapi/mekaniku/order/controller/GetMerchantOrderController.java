package academy.doku.da3duawebserviceapi.mekaniku.order.controller;

import academy.doku.da3duawebserviceapi.common.dto.BaseResponse;
import academy.doku.da3duawebserviceapi.common.utils.CheckRoleUtil;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.OrderResponse;
import academy.doku.da3duawebserviceapi.mekaniku.order.service.GetMerchantOrderByIdService;
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
public class GetMerchantOrderController {

    private final GetMerchantOrderByIdService getMerchantOrderByIdService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<BaseResponse<OrderResponse>> getAllTransactionPagination(
            @PathVariable UUID orderId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkCustomerForbiddenAccess(authentication);

        return ResponseEntity.ok(BaseResponse.<OrderResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Merchant order with ID: " + orderId)
                .data(getMerchantOrderByIdService.getMerchantOrder(orderId))
                .build());
    }
}
