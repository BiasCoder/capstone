package academy.doku.da3duawebserviceapi.mekaniku.report.service;

import academy.doku.da3duawebserviceapi.mekaniku.report.dto.ReportDiagramResponse;

import java.util.List;

public interface GetCustomerReportDiagramService {

    List<ReportDiagramResponse> getCustomerDiagram(Integer customerId, Integer timeframe);
}
