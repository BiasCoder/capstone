package academy.doku.da3duawebserviceapi.mekaniku.report.controller;

import academy.doku.da3duawebserviceapi.common.dto.BaseResponse;
import academy.doku.da3duawebserviceapi.common.utils.CheckRoleUtil;
import academy.doku.da3duawebserviceapi.mekaniku.report.dto.ReportDiagramResponse;
import academy.doku.da3duawebserviceapi.mekaniku.report.service.GetCustomerReportDiagramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class GetCustomerReportDiagramController {

    private final GetCustomerReportDiagramService getCustomerReportDiagramService;

    @CrossOrigin(origins = "*",allowedHeaders = "*")
    @GetMapping("customers/{customerId}/diagrams")
    public ResponseEntity<BaseResponse<List<ReportDiagramResponse>>> getCustomerDiagram(
            @PathVariable Integer customerId,
            @RequestParam(name = "timeframe", defaultValue = "7") Integer timeframe
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CheckRoleUtil.checkSuperAdminOnlyAccess(authentication);

        return ResponseEntity.ok(BaseResponse.<List<ReportDiagramResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Report diagram for Customer with ID:" + customerId)
                .data(getCustomerReportDiagramService.getCustomerDiagram(customerId, timeframe))
                .build());
    }
}
