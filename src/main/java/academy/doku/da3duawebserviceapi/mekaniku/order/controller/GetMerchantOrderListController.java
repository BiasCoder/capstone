package academy.doku.da3duawebserviceapi.mekaniku.order.controller;

import academy.doku.da3duawebserviceapi.common.dto.BaseResponse;
import academy.doku.da3duawebserviceapi.common.dto.PaginationResponse;
import academy.doku.da3duawebserviceapi.common.utils.CheckRoleUtil;
import academy.doku.da3duawebserviceapi.mekaniku.order.service.GetMerchantOrderListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@CrossOrigin
public class GetMerchantOrderListController {

    private final GetMerchantOrderListService getMerchantOrderListService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/workshops/{workshopId}/orders")
    public ResponseEntity<BaseResponse<PaginationResponse>> getAllTransactionPagination(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "status", defaultValue = "BOOKED") String status,
            @PathVariable Integer workshopId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkCustomerForbiddenAccess(authentication);

        Pageable pageable = PageRequest.of(page, limit);

        return ResponseEntity.ok(BaseResponse.<PaginationResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Merchant order list")
                .data(getMerchantOrderListService.getMerchantOrderList(pageable, workshopId, status))
                .build());
    }
}
