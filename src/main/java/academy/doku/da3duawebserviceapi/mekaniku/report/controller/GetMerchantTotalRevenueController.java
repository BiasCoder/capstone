package academy.doku.da3duawebserviceapi.mekaniku.report.controller;

import academy.doku.da3duawebserviceapi.common.dto.BaseResponse;
import academy.doku.da3duawebserviceapi.common.utils.CheckRoleUtil;
import academy.doku.da3duawebserviceapi.mekaniku.report.service.GetMerchantTotalRevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class GetMerchantTotalRevenueController {
    private final GetMerchantTotalRevenueService getMerchantTotalRevenueService;

    @CrossOrigin(origins = "*",allowedHeaders = "*")
    @GetMapping("workshops/{workshopId}/revenues")
    public ResponseEntity<BaseResponse<String>> getAllTransactionPagination(
            @PathVariable Integer workshopId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkCustomerForbiddenAccess(authentication);

        return ResponseEntity.ok(BaseResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Total revenues for Workshop with ID:" + workshopId)
                .data(BigDecimal.valueOf(getMerchantTotalRevenueService.getRevenue(workshopId)).toPlainString())
                .build());
    }

}
