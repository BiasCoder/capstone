package academy.doku.da3duawebserviceapi.mekaniku.report.service;

import academy.doku.da3duawebserviceapi.common.dto.PaginationResponse;
import academy.doku.da3duawebserviceapi.mekaniku.report.dto.ReportResponse;
import org.springframework.data.domain.Pageable;

public interface GetCustomerReportService {

    PaginationResponse<ReportResponse> getCustomerReport(Pageable pageable, Integer customerId);
}
