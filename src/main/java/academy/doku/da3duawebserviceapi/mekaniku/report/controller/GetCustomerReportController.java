package academy.doku.da3duawebserviceapi.mekaniku.report.controller;

import academy.doku.da3duawebserviceapi.common.dto.BaseResponse;
import academy.doku.da3duawebserviceapi.common.dto.PaginationResponse;
import academy.doku.da3duawebserviceapi.common.utils.CheckRoleUtil;
import academy.doku.da3duawebserviceapi.mekaniku.report.dto.ReportResponse;
import academy.doku.da3duawebserviceapi.mekaniku.report.service.GetCustomerReportService;
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
public class GetCustomerReportController {

    private final GetCustomerReportService getCustomerReportService;

    @CrossOrigin(origins = "*",allowedHeaders = "*")
    @GetMapping("customers/{customerId}/reports")
    public ResponseEntity<BaseResponse<PaginationResponse<ReportResponse>>> getAllTransactionPagination(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @PathVariable Integer customerId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkSuperAdminOnlyAccess(authentication);

        Pageable pageable = PageRequest.of(page, limit);

        return ResponseEntity.ok(BaseResponse.<PaginationResponse<ReportResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Report list for Customer with ID:" + customerId)
                .data(getCustomerReportService.getCustomerReport(pageable, customerId))
                .build());
    }
}
