package academy.doku.da3duawebserviceapi.mekaniku.report.service.impl;

import academy.doku.da3duawebserviceapi.common.dto.PaginationResponse;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.OrderEntity;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.enums.OrderStatus;
import academy.doku.da3duawebserviceapi.mekaniku.order.repository.OrderRepository;
import academy.doku.da3duawebserviceapi.mekaniku.report.dto.PaymentReportResponse;
import academy.doku.da3duawebserviceapi.mekaniku.report.dto.ReportDiagramResponse;
import academy.doku.da3duawebserviceapi.mekaniku.report.dto.ReportResponse;
import academy.doku.da3duawebserviceapi.mekaniku.report.entity.PaymentEntity;
import academy.doku.da3duawebserviceapi.mekaniku.report.entity.enums.PaymentStatus;
import academy.doku.da3duawebserviceapi.mekaniku.report.repository.PaymentRepository;
import academy.doku.da3duawebserviceapi.mekaniku.report.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionReportServiceImpl implements GetMerchantReportService, GetMerchantReportDiagramService,
        GetCustomerReportService, GetCustomerReportDiagramService,
        GetMerchantTotalRevenueService, GetCustomerTotalSpentService {
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    List<OrderStatus> statusList = new ArrayList<>(
            Arrays.asList(OrderStatus.DONE, OrderStatus.CANCELED, OrderStatus.REJECTED)
    );

    @Override
    public PaginationResponse getMerchantReport(Pageable pageable, Integer workshopId) {
        Page<OrderEntity> completedOrders = orderRepository.findAllByWorkshopIdAndStatusIn(pageable, workshopId, statusList);
        List<ReportResponse> merchantReportList = completedOrders.stream().map(report -> {
            ReportResponse mappedReport = modelMapper.map(report, ReportResponse.class);

            // Set invoice by payment
            if (report.getStatus().equals(OrderStatus.DONE)) {
                Optional<PaymentEntity> payment = paymentRepository.findByOrderIdAndStatus(report.getId(), PaymentStatus.SUCCESS);

                if (payment.isPresent()) {
                    PaymentReportResponse invoice = new PaymentReportResponse();
                    invoice.setPaymentName(payment.get().getPaymentMethod().getDescription());
                    invoice.setPaymentImageUrl(payment.get().getPaymentMethod().getImageUrl());

                    mappedReport.setInvoiceNumber(payment.get().getInvoiceNumber());
                    mappedReport.setInvoice(invoice);
                }
            }

            // Set total price
            mappedReport.setTotalPrice("0");

            if (report.getTotalPrice() != null) {
                mappedReport.setTotalPrice(BigDecimal.valueOf(report.getTotalPrice()).toPlainString());
            }

            return mappedReport;
        }).toList();

        return PaginationResponse.builder()
                .totalItem(completedOrders.getTotalElements())
                .totalPage(completedOrders.getTotalPages())
                .currentPage(completedOrders.getNumber() + 1)
                .list(merchantReportList)
                .build();
    }

    @Override
    public PaginationResponse getCustomerReport(Pageable pageable, Integer customerId) {

        Page<OrderEntity> completedOrders = orderRepository.findAllByCustomerIdAndStatusIn(pageable, customerId, statusList);
        List<ReportResponse> customerReportList = completedOrders.stream().map(report -> {
            ReportResponse mappedReport = modelMapper.map(report, ReportResponse.class);

            // Set invoice by payment
            if (report.getStatus().equals(OrderStatus.DONE)) {
                Optional<PaymentEntity> payment = paymentRepository.findByOrderIdAndStatus(report.getId(), PaymentStatus.SUCCESS);

                if (payment.isPresent()) {
                    PaymentReportResponse invoice = new PaymentReportResponse();
                    invoice.setPaymentName(payment.get().getPaymentMethod().getDescription());
                    invoice.setPaymentImageUrl(payment.get().getPaymentMethod().getImageUrl());

                    mappedReport.setInvoiceNumber(payment.get().getInvoiceNumber());
                    mappedReport.setInvoice(invoice);
                }
            }

            // Set total price
            mappedReport.setTotalPrice("0");

            if (report.getTotalPrice() != null) {
                mappedReport.setTotalPrice(BigDecimal.valueOf(report.getTotalPrice()).toPlainString());
            }

            return mappedReport;
        }).toList();

        return PaginationResponse.builder()
                .totalItem(completedOrders.getTotalElements())
                .totalPage(completedOrders.getTotalPages())
                .currentPage(completedOrders.getNumber() + 1)
                .list(customerReportList)
                .build();
    }

    @Override
    public List<ReportDiagramResponse> getMerchantDiagram(Integer workshopId, Integer timeframe) {

        List<ReportDiagramResponse> reportList = new ArrayList<>();

        for (long i = timeframe - 1L; i >= 0 ; i--) {

            List<OrderEntity> ordersPerDay = orderRepository.findAllByWorkshopIdAndStatusInAndModifiedAtBetween(workshopId, statusList,
                    LocalDateTime.of(LocalDate.now().minusDays(i), LocalTime.MIDNIGHT),
                    LocalDateTime.of(LocalDate.now().minusDays(i-1), LocalTime.MIDNIGHT));

            ReportDiagramResponse res = new ReportDiagramResponse();

            int success = 0;
            int canceled = 0;
            int rejected = 0;

            for (OrderEntity order: ordersPerDay) {
                if (order.getStatus().equals(OrderStatus.DONE)) {
                    success++;
                }
                if (order.getStatus().equals(OrderStatus.CANCELED)) {
                    canceled++;
                }
                if (order.getStatus().equals(OrderStatus.REJECTED)) {
                    rejected++;
                }
            }

            res.setDate(LocalDateTime.now().minusDays(i));
            res.setSuccess(success);
            res.setCanceled(canceled);
            res.setRejected(rejected);

            reportList.add(res);
        }

        return reportList;
    }


    @Override
    public List<ReportDiagramResponse> getCustomerDiagram(Integer customerId, Integer timeframe) {


        List<ReportDiagramResponse> reportList = new ArrayList<>();

        for (long i = timeframe - 1L; i >= 0 ; i--) {

            List<OrderEntity> ordersPerDay = orderRepository.findAllByCustomerIdAndStatusInAndModifiedAtBetween(customerId, statusList,
                    LocalDateTime.of(LocalDate.now().minusDays(i), LocalTime.MIDNIGHT),
                    LocalDateTime.of(LocalDate.now().minusDays(i-1), LocalTime.MIDNIGHT));

            ReportDiagramResponse res = new ReportDiagramResponse();

            int success = 0;
            int canceled = 0;
            int rejected = 0;

            for (OrderEntity order: ordersPerDay) {
                if (order.getStatus().equals(OrderStatus.DONE)) {
                    success++;
                }
                if (order.getStatus().equals(OrderStatus.CANCELED)) {
                    canceled++;
                }
                if (order.getStatus().equals(OrderStatus.REJECTED)) {
                    rejected++;
                }
            }

            res.setDate(LocalDateTime.now().minusDays(i));
            res.setSuccess(success);
            res.setCanceled(canceled);
            res.setRejected(rejected);

            reportList.add(res);
        }

        return reportList;
    }

    @Override
    public Double getRevenue(Integer workshopId) {
        List<OrderEntity> completedOrders = orderRepository.findAllByWorkshopIdAndStatusIn(workshopId, statusList);
        double totalRevenue = 0.0;

        for (OrderEntity order: completedOrders) {
            if (order.getTotalPrice() != null) {
                totalRevenue += order.getTotalPrice();
            }
        }

        return totalRevenue;
    }

    @Override
    public Double getSpent(Integer customerId) {
        List<OrderEntity> completedOrders = orderRepository.findAllByCustomerIdAndStatusIn(customerId, statusList);
        double totalSpent = 0.0;

        for (OrderEntity order: completedOrders) {
            if (order.getTotalPrice() != null) {
                totalSpent += order.getTotalPrice();
            }
        }

        return totalSpent;
    }
}
