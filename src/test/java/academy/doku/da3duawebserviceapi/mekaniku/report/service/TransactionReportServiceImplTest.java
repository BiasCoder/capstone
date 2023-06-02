package academy.doku.da3duawebserviceapi.mekaniku.report.service;

import academy.doku.da3duawebserviceapi.common.dto.PaginationResponse;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.OrderEntity;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.enums.OrderStatus;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.enums.VehicleType;
import academy.doku.da3duawebserviceapi.mekaniku.order.repository.OrderRepository;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.entity.PaymentCategory;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.entity.PaymentMethodEntity;
import academy.doku.da3duawebserviceapi.mekaniku.report.dto.ReportDiagramResponse;
import academy.doku.da3duawebserviceapi.mekaniku.report.entity.PaymentEntity;
import academy.doku.da3duawebserviceapi.mekaniku.report.entity.enums.PaymentStatus;
import academy.doku.da3duawebserviceapi.mekaniku.report.repository.PaymentRepository;
import academy.doku.da3duawebserviceapi.mekaniku.report.service.impl.TransactionReportServiceImpl;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserEntity;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserRole;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransactionReportServiceImplTest {

    @InjectMocks
    private TransactionReportServiceImpl transactionReportService;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PaymentRepository paymentRepository;

    @Test
    void get_merchant_report_list() {
        UUID paymentId = UUID.fromString("d1d25ee5-43a0-4ce7-a677-38b634be6d75");

        List<OrderEntity> expectedData = new ArrayList<>();

        List<OrderStatus> statusList = new ArrayList<>(
                Arrays.asList(OrderStatus.DONE, OrderStatus.CANCELED, OrderStatus.REJECTED)
        );

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime bookedTime = LocalDateTime.of(2023, Month.JANUARY, 25, 12, 10);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);
        PaymentMethodEntity paymentMethod = new PaymentMethodEntity(1, "EMONEY_OVO", "OVO", PaymentCategory.EMONEY, true, "dasdadsa", createdDate, modifiedDate);

        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75"), customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.DONE, bookedTime, null, null, null, null, bookedTime.plusHours(5), null, createdDate, modifiedDate, 0.0));
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d76"), customer, workshop, null, "ORDER-12345", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.REJECTED, bookedTime, null, null, null, null, null, bookedTime.plusHours(1), createdDate, modifiedDate, 0.0));
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d77"), customer, workshop, null, "ORDER-67891", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.CANCELED, bookedTime, bookedTime.plusHours(1), null, null, null, null, null, createdDate, modifiedDate, 0.0));

        PaymentEntity paymentData = new PaymentEntity(paymentId, paymentMethod, expectedData.get(0), "INV-12313", "dasdaw", PaymentStatus.SUCCESS, true, createdDate, modifiedDate);

        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderEntity> orderPage = new PageImpl<>(expectedData, pageable, 0);

        when(orderRepository.findAllByWorkshopIdAndStatusIn(pageable, workshop.getId(), statusList)).thenReturn(orderPage);
        when(paymentRepository.findByOrderIdAndStatus(expectedData.get(0).getId(), PaymentStatus.SUCCESS)).thenReturn(Optional.of(paymentData));

        PaginationResponse reportList = transactionReportService.getMerchantReport(pageable, workshop.getId());

        verify(orderRepository, times(1)).findAllByWorkshopIdAndStatusIn(pageable, workshop.getId(), statusList);

        assertEquals(expectedData.size(), reportList.getTotalItem());
        assertEquals(1, reportList.getCurrentPage());
        assertEquals(1, reportList.getTotalPage());
    }

    @Test
    void get_customer_report_list() {
        UUID paymentId = UUID.fromString("d1d25ee5-43a0-4ce7-a677-38b634be6d75");

        List<OrderEntity> expectedData = new ArrayList<>();

        List<OrderStatus> statusList = new ArrayList<>(
                Arrays.asList(OrderStatus.DONE, OrderStatus.CANCELED, OrderStatus.REJECTED)
        );

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime bookedTime = LocalDateTime.of(2023, Month.JANUARY, 25, 12, 10);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);
        PaymentMethodEntity paymentMethod = new PaymentMethodEntity(1, "EMONEY_OVO", "OVO", PaymentCategory.EMONEY, true, "dasdadsa", createdDate, modifiedDate);

        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75"), customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.DONE, bookedTime, null, null, null, null, bookedTime.plusHours(5), null, createdDate, modifiedDate, 0.0));
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d76"), customer, workshop, null, "ORDER-12345", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.REJECTED, bookedTime, null, null, null, null, null, bookedTime.plusHours(1), createdDate, modifiedDate, 0.0));
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d77"), customer, workshop, null, "ORDER-67891", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.CANCELED, bookedTime, bookedTime.plusHours(1), null, null, null, null, null, createdDate, modifiedDate, 0.0));

        PaymentEntity paymentData = new PaymentEntity(paymentId, paymentMethod, expectedData.get(0), "INV-12313", "dasdaw", PaymentStatus.SUCCESS, true, createdDate, modifiedDate);

        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderEntity> orderPage = new PageImpl<>(expectedData, pageable, 0);

        when(orderRepository.findAllByCustomerIdAndStatusIn(pageable, customer.getId(), statusList)).thenReturn(orderPage);
        when(paymentRepository.findByOrderIdAndStatus(expectedData.get(0).getId(), PaymentStatus.SUCCESS)).thenReturn(Optional.of(paymentData));

        PaginationResponse reportList = transactionReportService.getCustomerReport(pageable, customer.getId());

        verify(orderRepository, times(1)).findAllByCustomerIdAndStatusIn(pageable, customer.getId(), statusList);

        assertEquals(expectedData.size(), reportList.getTotalItem());
        assertEquals(1, reportList.getCurrentPage());
        assertEquals(1, reportList.getTotalPage());
    }

    @Test
    void get_merchant_diagram() {
        List<OrderEntity> expectedData = new ArrayList<>();

        List<OrderStatus> statusList = new ArrayList<>(
                Arrays.asList(OrderStatus.DONE, OrderStatus.CANCELED, OrderStatus.REJECTED)
        );

        // Create Order in 2 days before now
        LocalDateTime createdDate = LocalDateTime.now().minusDays(2);
        LocalDateTime modifiedDate = LocalDateTime.now().minusDays(2);
        LocalDateTime bookedTime = LocalDateTime.now().minusDays(2);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);

        // DONE Order (2 order)
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75"), customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.DONE, bookedTime, null, null, null, null, bookedTime.plusHours(5), null, createdDate, modifiedDate, 0.0));
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d71"), customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.DONE, bookedTime, null, null, null, null, bookedTime.plusHours(5), null, createdDate, modifiedDate, 0.0));
        // REJECTED Order (1 order)
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d76"), customer, workshop, null, "ORDER-12345", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.REJECTED, bookedTime, null, null, null, null, null, bookedTime.plusHours(1), createdDate, modifiedDate, 0.0));
        // CANCELED Order (1 order)
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d77"), customer, workshop, null, "ORDER-67891", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.CANCELED, bookedTime, bookedTime.plusHours(1), null, null, null, null, null, createdDate, modifiedDate, 0.0));

        when(orderRepository.findAllByWorkshopIdAndStatusInAndModifiedAtBetween(workshop.getId(), statusList, LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.MIDNIGHT), LocalDateTime.of(LocalDate.now().minusDays(2-1), LocalTime.MIDNIGHT))).thenReturn(expectedData);

        List<ReportDiagramResponse> diagramList = transactionReportService.getMerchantDiagram(workshop.getId(), 7);

        // Expected order quantity (DONE, REJECTED, CANCELED)
        assertEquals(2 , diagramList.get(4).getSuccess());
        assertEquals(1, diagramList.get(4).getCanceled());
        assertEquals(1, diagramList.get(4).getRejected());
    }

    @Test
    void get_customer_diagram() {
        List<OrderEntity> expectedData = new ArrayList<>();

        List<OrderStatus> statusList = new ArrayList<>(
                Arrays.asList(OrderStatus.DONE, OrderStatus.CANCELED, OrderStatus.REJECTED)
        );

        // Create Order in 2 days before now
        LocalDateTime createdDate = LocalDateTime.now().minusDays(2);
        LocalDateTime modifiedDate = LocalDateTime.now().minusDays(2);
        LocalDateTime bookedTime = LocalDateTime.now().minusDays(2);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);

        // DONE Order (2 order)
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75"), customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.DONE, bookedTime, null, null, null, null, bookedTime.plusHours(5), null, createdDate, modifiedDate, 0.0));
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d71"), customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.DONE, bookedTime, null, null, null, null, bookedTime.plusHours(5), null, createdDate, modifiedDate, 0.0));
        // REJECTED Order (1 order)
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d76"), customer, workshop, null, "ORDER-12345", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.REJECTED, bookedTime, null, null, null, null, null, bookedTime.plusHours(1), createdDate, modifiedDate, 0.0));
        // CANCELED Order (2 order)
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d77"), customer, workshop, null, "ORDER-67891", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.CANCELED, bookedTime, bookedTime.plusHours(1), null, null, null, null, null, createdDate, modifiedDate, 0.0));
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d72"), customer, workshop, null, "ORDER-67891", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.CANCELED, bookedTime, bookedTime.plusHours(1), null, null, null, null, null, createdDate, modifiedDate, 0.0));

        when(orderRepository.findAllByCustomerIdAndStatusInAndModifiedAtBetween(customer.getId(), statusList, LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.MIDNIGHT), LocalDateTime.of(LocalDate.now().minusDays(2-1), LocalTime.MIDNIGHT))).thenReturn(expectedData);

        List<ReportDiagramResponse> diagramList = transactionReportService.getCustomerDiagram(customer.getId(), 7);

        // Expected order quantity (DONE, REJECTED, CANCELED)
        assertEquals(2 , diagramList.get(4).getSuccess());
        assertEquals(1, diagramList.get(4).getRejected());
        assertEquals(2, diagramList.get(4).getCanceled());
    }

    @Test
    void get_merchant_total_revenue() {
        List<OrderEntity> expectedData = new ArrayList<>();

        List<OrderStatus> statusList = new ArrayList<>(
                Arrays.asList(OrderStatus.DONE, OrderStatus.CANCELED, OrderStatus.REJECTED)
        );

        // Create Order in 2 days before now
        LocalDateTime createdDate = LocalDateTime.now().minusDays(2);
        LocalDateTime modifiedDate = LocalDateTime.now().minusDays(2);
        LocalDateTime bookedTime = LocalDateTime.now().minusDays(2);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);

        // DONE Order (totalPrice = Rp.350.000)
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75"), customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.DONE, bookedTime, null, null, null, null, bookedTime.plusHours(5), null, createdDate, modifiedDate, 250000.0));
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d71"), customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.DONE, bookedTime, null, null, null, null, bookedTime.plusHours(5), null, createdDate, modifiedDate, 100000.0));
        // REJECTED Order (1 order)
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d76"), customer, workshop, null, "ORDER-12345", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.REJECTED, bookedTime, null, null, null, null, null, bookedTime.plusHours(1), createdDate, modifiedDate, 0.0));
        // CANCELED Order (1 order)
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d77"), customer, workshop, null, "ORDER-67891", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.CANCELED, bookedTime, bookedTime.plusHours(1), null, null, null, null, null, createdDate, modifiedDate, 0.0));

        when(orderRepository.findAllByWorkshopIdAndStatusIn(workshop.getId(), statusList)).thenReturn(expectedData);

        Double totalRevenue = transactionReportService.getRevenue(workshop.getId());

        verify(orderRepository, times(1)).findAllByWorkshopIdAndStatusIn(workshop.getId(), statusList);

        assertEquals(350000.0, totalRevenue);
    }

    @Test
    void get_customer_total_spent() {
        List<OrderEntity> expectedData = new ArrayList<>();

        List<OrderStatus> statusList = new ArrayList<>(
                Arrays.asList(OrderStatus.DONE, OrderStatus.CANCELED, OrderStatus.REJECTED)
        );

        // Create Order in 2 days before now
        LocalDateTime createdDate = LocalDateTime.now().minusDays(2);
        LocalDateTime modifiedDate = LocalDateTime.now().minusDays(2);
        LocalDateTime bookedTime = LocalDateTime.now().minusDays(2);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);

        // DONE Order (totalPrice = Rp.375.000)
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75"), customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.DONE, bookedTime, null, null, null, null, bookedTime.plusHours(5), null, createdDate, modifiedDate, 250000.0));
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d71"), customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.DONE, bookedTime, null, null, null, null, bookedTime.plusHours(5), null, createdDate, modifiedDate, 100000.0));
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d71"), customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.DONE, bookedTime, null, null, null, null, bookedTime.plusHours(5), null, createdDate, modifiedDate, 25000.0));
        // REJECTED Order (1 order)
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d76"), customer, workshop, null, "ORDER-12345", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.REJECTED, bookedTime, null, null, null, null, null, bookedTime.plusHours(1), createdDate, modifiedDate, 0.0));

        when(orderRepository.findAllByCustomerIdAndStatusIn(customer.getId(), statusList)).thenReturn(expectedData);

        Double totalSpent = transactionReportService.getSpent(customer.getId());

        verify(orderRepository, times(1)).findAllByCustomerIdAndStatusIn(customer.getId(), statusList);

        assertEquals(375000.0, totalSpent);
    }
}