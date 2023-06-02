package academy.doku.da3duawebserviceapi.mekaniku.report.service;

import academy.doku.da3duawebserviceapi.mekaniku.report.dto.ReportDiagramResponse;

import java.util.List;

public interface GetMerchantReportDiagramService {
    List<ReportDiagramResponse> getMerchantDiagram(Integer workshopId, Integer timeframe);
}
