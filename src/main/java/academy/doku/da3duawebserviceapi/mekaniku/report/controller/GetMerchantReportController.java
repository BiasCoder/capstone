package academy.doku.da3duawebserviceapi.mekaniku.report.controller;

import academy.doku.da3duawebserviceapi.common.dto.BaseResponse;
import academy.doku.da3duawebserviceapi.common.dto.PaginationResponse;
import academy.doku.da3duawebserviceapi.common.utils.CheckRoleUtil;
import academy.doku.da3duawebserviceapi.mekaniku.report.dto.ReportResponse;
import academy.doku.da3duawebserviceapi.mekaniku.report.service.GetMerchantReportService;
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
public class GetMerchantReportController {

    private final GetMerchantReportService getMerchantReportService;

    @CrossOrigin(origins = "*",allowedHeaders = "*")
    @GetMapping("workshops/{workshopId}/reports")
    public ResponseEntity<BaseResponse<PaginationResponse<ReportResponse>>> getAllTransactionPagination(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @PathVariable Integer workshopId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkCustomerForbiddenAccess(authentication);

        Pageable pageable = PageRequest.of(page, limit);

        return ResponseEntity.ok(BaseResponse.<PaginationResponse<ReportResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Report list for Workshop with ID:" + workshopId)

                .data(getMerchantReportService.getMerchantReport(pageable, workshopId))
                .build());
    }
}
