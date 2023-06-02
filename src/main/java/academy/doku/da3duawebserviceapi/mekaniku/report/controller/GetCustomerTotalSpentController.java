package academy.doku.da3duawebserviceapi.mekaniku.report.controller;

import academy.doku.da3duawebserviceapi.common.dto.BaseResponse;
import academy.doku.da3duawebserviceapi.common.utils.CheckRoleUtil;
import academy.doku.da3duawebserviceapi.mekaniku.report.service.GetCustomerTotalSpentService;
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
public class GetCustomerTotalSpentController {

    private final GetCustomerTotalSpentService getCustomerTotalSpentService;

    @CrossOrigin(origins = "*",allowedHeaders = "*")
    @GetMapping("customers/{customerId}/spent")
    public ResponseEntity<BaseResponse<String>> getAllTransactionPagination(
            @PathVariable Integer customerId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkSuperAdminOnlyAccess(authentication);

        return ResponseEntity.ok(BaseResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Total spent for Customer with ID:" + customerId)
                .data(BigDecimal.valueOf(getCustomerTotalSpentService.getSpent(customerId)).toPlainString())
                .build());
    }
}
