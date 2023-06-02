package academy.doku.da3duawebserviceapi.mekaniku.report.service;

import academy.doku.da3duawebserviceapi.common.dto.PaginationResponse;
import academy.doku.da3duawebserviceapi.mekaniku.report.dto.ReportResponse;
import org.springframework.data.domain.Pageable;

public interface GetMerchantReportService {
    PaginationResponse<ReportResponse> getMerchantReport(Pageable pageable, Integer workshopId);
}
