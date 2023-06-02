package academy.doku.da3duawebserviceapi.mekaniku.order.service;

import academy.doku.da3duawebserviceapi.common.dto.PaginationResponse;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.OrderResponse;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.OrderStatusRequest;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.UpdateOrderDetailRequest;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.UpdateOrderRequest;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.OrderDetailEntity;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.OrderEntity;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.enums.OrderStatus;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.enums.VehicleType;
import academy.doku.da3duawebserviceapi.mekaniku.order.exception.InvalidStatusParamException;
import academy.doku.da3duawebserviceapi.mekaniku.order.exception.OrderNotFoundException;
import academy.doku.da3duawebserviceapi.mekaniku.order.exception.RejectionNoteEmptyException;
import academy.doku.da3duawebserviceapi.mekaniku.order.repository.OrderDetailRepository;
import academy.doku.da3duawebserviceapi.mekaniku.order.repository.OrderRepository;
import academy.doku.da3duawebserviceapi.mekaniku.order.service.impl.OrderServiceImpl;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.entity.PaymentCategory;
import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.entity.PaymentMethodEntity;
import academy.doku.da3duawebserviceapi.mekaniku.report.entity.PaymentEntity;
import academy.doku.da3duawebserviceapi.mekaniku.report.entity.enums.PaymentStatus;
import academy.doku.da3duawebserviceapi.mekaniku.report.repository.PaymentRepository;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserEntity;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserRole;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.ProductType;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopProductEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.repository.WorkshopProductRepository;
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
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private WorkshopProductRepository workshopProductRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Test
    void get_merchant_order_list() {
        UUID paymentId = UUID.fromString("d1d25ee5-43a0-4ce7-a677-38b634be6d75");

        List<OrderEntity> expectedData = new ArrayList<>();

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime bookedTime = LocalDateTime.of(2023, Month.JANUARY, 25, 12, 10);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);
        PaymentMethodEntity paymentMethod = new PaymentMethodEntity(1, "EMONEY_OVO", "OVO", PaymentCategory.EMONEY, true, "dasdadsa", createdDate, modifiedDate);

        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75"), customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.DONE, bookedTime, null, null, null, null, null, null, createdDate, modifiedDate, 0.0));
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d76"), customer, workshop, null, "ORDER-12345", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.BOOKED, bookedTime, null, null, null, null, null, null, createdDate, modifiedDate, 0.0));
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d77"), customer, workshop, null, "ORDER-67891", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.BOOKED, bookedTime, null, null, null, null, null, null, createdDate, modifiedDate, 0.0));
        expectedData.add(new OrderEntity(UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d77"), customer, workshop, null, "ORDER-67891", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.CANCELED, bookedTime, bookedTime.plusHours(1), null, null, null, null, null, createdDate, modifiedDate, 0.0));

        PaymentEntity paymentData = new PaymentEntity(paymentId, paymentMethod, expectedData.get(0), "INV-12313", "dasdaw", PaymentStatus.SUCCESS, true, createdDate, modifiedDate);

        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderEntity> orderPage = new PageImpl<>(expectedData, pageable, 0);

        when(orderRepository.findAllByWorkshopIdAndStatusOrderByCreatedAtDesc(pageable, workshop.getId(), OrderStatus.BOOKED)).thenReturn(orderPage);
        when(paymentRepository.findByOrderIdAndStatus(expectedData.get(0).getId(), PaymentStatus.SUCCESS)).thenReturn(Optional.of(paymentData));

        PaginationResponse orderList = orderService.getMerchantOrderList(pageable, workshop.getId(), "BOOKED");

//        assertEquals(expectedData.get(0), orderList.getList().get(0));
//        assertEquals(expectedData.get(0).getId(), firstOrder.getId());
//        assertEquals(expectedData.get(0).getStatus(), firstOrder.getStatus());
//        assertEquals(expectedData.get(0).getOrderNumber(), firstOrder.getOrderNumber());

        verify(orderRepository, times(1)).findAllByWorkshopIdAndStatusOrderByCreatedAtDesc(pageable, workshop.getId(), OrderStatus.BOOKED);
        verify(paymentRepository, times(1)).findByOrderIdAndStatus(expectedData.get(0).getId(), PaymentStatus.SUCCESS);

        assertEquals(expectedData.size(), orderList.getTotalItem());
        assertEquals(1, orderList.getCurrentPage());
        assertEquals(1, orderList.getTotalPage());
    }

    @Test
    void get_merchant_order_by_id() {
        // Given
        UUID orderId = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75");
        UUID paymentId = UUID.fromString("d1d25ee5-43a0-4ce7-a677-38b634be6d75");

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime bookedTime = LocalDateTime.of(2023, Month.JANUARY, 25, 12, 10);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);
        PaymentMethodEntity paymentMethod = new PaymentMethodEntity(1, "EMONEY_OVO", "OVO", PaymentCategory.EMONEY, true, "dasdadsa", createdDate, modifiedDate);

        OrderEntity expectedData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.DONE, bookedTime, null, null, null, null, bookedTime.plusHours(3), null, createdDate, modifiedDate, 0.0);
        PaymentEntity paymentData = new PaymentEntity(paymentId, paymentMethod, expectedData, "INV-12313", "dasdaw", PaymentStatus.SUCCESS, true, createdDate, modifiedDate);

        // When
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedData));
        when(paymentRepository.findByOrderIdAndStatus(orderId, PaymentStatus.SUCCESS)).thenReturn(Optional.of(paymentData));

        OrderResponse merchantOrder = orderService.getMerchantOrder(orderId);

        // Then
        verify(orderRepository, times(1)).findById(orderId);
        verify(paymentRepository, times(1)).findByOrderIdAndStatus(orderId, PaymentStatus.SUCCESS);

        assertNotNull(merchantOrder.getId());

        assertEquals(expectedData.getId(), merchantOrder.getId());
        assertEquals(expectedData.getStatus(), merchantOrder.getStatus());
        assertEquals(expectedData.getOrderNumber(), merchantOrder.getOrderNumber());

    }

    @Test
    void get_merchant_order_by_invalid_id() {
        // Given
        UUID invalidOrderId = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d77");

        UUID orderId = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75");
        UUID paymentId = UUID.fromString("d1d25ee5-43a0-4ce7-a677-38b634be6d75");

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime bookedTime = LocalDateTime.of(2023, Month.JANUARY, 25, 12, 10);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);
        PaymentMethodEntity paymentMethod = new PaymentMethodEntity(1, "EMONEY_OVO", "OVO", PaymentCategory.EMONEY, true, "dasdadsa", createdDate, modifiedDate);

        OrderEntity expectedData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.DONE, bookedTime, null, null, null, null, bookedTime.plusHours(3), null, createdDate, modifiedDate, 0.0);
        PaymentEntity paymentData = new PaymentEntity(paymentId, paymentMethod, expectedData, "INV-12313", "dasdaw", PaymentStatus.SUCCESS, true, createdDate, modifiedDate);

        // When
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedData));
        when(paymentRepository.findByOrderIdAndStatus(orderId, PaymentStatus.SUCCESS)).thenReturn(Optional.of(paymentData));

        OrderResponse merchantOrder = orderService.getMerchantOrder(orderId);

        // Then
        verify(orderRepository, times(1)).findById(orderId);
        verify(paymentRepository, times(1)).findByOrderIdAndStatus(orderId, PaymentStatus.SUCCESS);

        // THEN
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getMerchantOrder(invalidOrderId);
            verify(orderRepository, times(1)).findById(invalidOrderId);
        }, "OrderNotFoundException was expected");

    }

    @Test
    void update_merchant_order() {
        // GIVEN
        UUID orderId = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75");
        UUID orderDetailId = UUID.fromString("d1d25ee5-43a0-4ce1-a611-38b634be6d11");
        UUID productId = UUID.fromString("d1d25ee5-43a0-4ce1-a611-38b634be6d22");

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime bookedTime = LocalDateTime.of(2023, Month.JANUARY, 25, 12, 10);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);

        OrderEntity oldData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.BOOKED, bookedTime, null, null, null, null, null, null, createdDate, modifiedDate, 0.0);

        WorkshopProductEntity product = new WorkshopProductEntity(productId, workshop, "Ganti Oli", 45000.0, ProductType.SERVICE, createdDate, modifiedDate);

        UpdateOrderDetailRequest requestDetail = new UpdateOrderDetailRequest(productId, 1);
        List<UpdateOrderDetailRequest> detailList = new ArrayList<>();
        detailList.add(requestDetail);

        Set<OrderDetailEntity> orderDetails = new HashSet<>();
        OrderDetailEntity OrderDetail = new OrderDetailEntity(orderDetailId, oldData, product, "Ganti Oli", product.getPrice(), product.getType(), requestDetail.getQuantity(), createdDate, modifiedDate);
        orderDetails.add(OrderDetail);


        UpdateOrderRequest orderRequest = new UpdateOrderRequest(detailList, "Tambah Oli gan");

        OrderEntity expectedData = new OrderEntity(orderId, customer, workshop, orderDetails, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.ACCEPTED, bookedTime, null, bookedTime.plusMinutes(30), null, null, null, bookedTime.plusHours(1), createdDate, modifiedDate, 0.0);


        // WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(oldData));
        when(orderRepository.save(oldData)).thenReturn(expectedData);
        when(workshopProductRepository.findById(productId)).thenReturn(Optional.of(product));

        OrderResponse updatedOrder = orderService.updateOrder(orderId, orderRequest);

        // THEN
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderDetailRepository, times(1)).deleteAllByOrderId(orderId);
        verify(workshopProductRepository, times(1)).findById(productId);

        verify(orderRepository, times(1)).save(any(OrderEntity.class));

        assertEquals(OrderDetail.getName(), updatedOrder.getOrderDetails().get(0).getName());
        assertEquals(OrderDetail.getType(), updatedOrder.getOrderDetails().get(0).getType());
        assertEquals(OrderDetail.getPrice(), updatedOrder.getOrderDetails().get(0).getPrice());
    }

    @Test
    void update_merchant_order_invalid_id() {
        // GIVEN
        UUID orderId = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75");
        UUID orderDetailId = UUID.fromString("d1d25ee5-43a0-4ce1-a611-38b634be6d11");
        UUID productId = UUID.fromString("d1d25ee5-43a0-4ce1-a611-38b634be6d22");

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime bookedTime = LocalDateTime.of(2023, Month.JANUARY, 25, 12, 10);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);

        OrderEntity oldData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.BOOKED, bookedTime, null, null, null, null, null, null, createdDate, modifiedDate, 0.0);

        WorkshopProductEntity product = new WorkshopProductEntity(productId, workshop, "Ganti Oli", 45000.0, ProductType.SERVICE, createdDate, modifiedDate);

        UpdateOrderDetailRequest requestDetail = new UpdateOrderDetailRequest(productId, 1);
        List<UpdateOrderDetailRequest> detailList = new ArrayList<>();
        detailList.add(requestDetail);

        Set<OrderDetailEntity> orderDetails = new HashSet<>();
        OrderDetailEntity OrderDetail = new OrderDetailEntity(orderDetailId, oldData, product, "Ganti Oli", product.getPrice(), product.getType(), requestDetail.getQuantity(), createdDate, modifiedDate);
        orderDetails.add(OrderDetail);


        UpdateOrderRequest orderRequest = new UpdateOrderRequest(detailList, "Tambah Oli gan");


        // WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(oldData));

        // THEN
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.updateOrder(productId, orderRequest);
            verify(orderRepository, times(1)).findById(orderId);
        }, "OrderNotFoundException was expected");

    }

    @Test
    void update_order_status_accepted() {
        // GIVEN
        UUID orderId = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75");

        OrderStatusRequest request = new OrderStatusRequest("ACCEPTED", null);

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime bookedTime = LocalDateTime.of(2023, Month.JANUARY, 25, 12, 10);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);

        OrderEntity oldData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.BOOKED, bookedTime, null, null, null, null, null, null, createdDate, modifiedDate, 0.0);
        OrderEntity expectedData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.ACCEPTED, bookedTime, null, bookedTime.plusMinutes(30), null, null, null, bookedTime.plusHours(1), createdDate, modifiedDate, 0.0);

        // WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(oldData));
        when(orderRepository.save(oldData)).thenReturn(expectedData);

        OrderResponse updatedStatusOrder = orderService.updateStatus(orderId, request);

        // THEN
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));

        assertEquals(OrderStatus.ACCEPTED, updatedStatusOrder.getStatus());
    }

    @Test
    void update_order_status_canceled() {
        // GIVEN
        UUID orderId = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75");

        OrderStatusRequest request = new OrderStatusRequest("CANCELED", null);

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime bookedTime = LocalDateTime.of(2023, Month.JANUARY, 25, 12, 10);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);

        OrderEntity oldData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.BOOKED, bookedTime, null, null, null, null, null, null, createdDate, modifiedDate, 0.0);
        OrderEntity expectedData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.CANCELED, bookedTime, bookedTime.plusHours(1), null, null, null, null, bookedTime.plusMinutes(30), createdDate, modifiedDate, 0.0);

        // WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(oldData));
        when(orderRepository.save(oldData)).thenReturn(expectedData);

        OrderResponse updatedStatusOrder = orderService.updateStatus(orderId, request);

        // THEN
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));

        assertEquals(OrderStatus.CANCELED, updatedStatusOrder.getStatus());
    }

    @Test
    void update_order_status_in_process() {
        // GIVEN
        UUID orderId = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75");

        OrderStatusRequest request = new OrderStatusRequest("IN_PROCESS", null);

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime bookedTime = LocalDateTime.of(2023, Month.JANUARY, 25, 12, 10);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);

        OrderEntity oldData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.BOOKED, bookedTime, null, null, null, null, null, null, createdDate, modifiedDate, 0.0);
        OrderEntity expectedData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.CANCELED, bookedTime, bookedTime.plusHours(1), null, null, null, null, bookedTime.plusMinutes(30), createdDate, modifiedDate, 0.0);

        // WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(oldData));
        when(orderRepository.save(oldData)).thenReturn(expectedData);

        OrderResponse updatedStatusOrder = orderService.updateStatus(orderId, request);

        // THEN
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));

        assertEquals(OrderStatus.CANCELED, updatedStatusOrder.getStatus());
    }

    @Test
    void update_order_status_processed() {
        // GIVEN
        UUID orderId = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75");

        OrderStatusRequest request = new OrderStatusRequest("PROCESSED", null);

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime bookedTime = LocalDateTime.of(2023, Month.JANUARY, 25, 12, 10);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);

        OrderEntity oldData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.BOOKED, bookedTime, null, null, null, null, null, null, createdDate, modifiedDate, 0.0);
        OrderEntity expectedData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.PROCESSED, bookedTime, null, null, null, bookedTime.plusHours(2), null, null, createdDate, modifiedDate, 0.0);

        // WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(oldData));
        when(orderRepository.save(oldData)).thenReturn(expectedData);

        OrderResponse updatedStatusOrder = orderService.updateStatus(orderId, request);

        // THEN
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));

        assertEquals(OrderStatus.PROCESSED, updatedStatusOrder.getStatus());
    }

    @Test
    void update_order_status_done() {
        // GIVEN
        UUID orderId = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75");

        OrderStatusRequest request = new OrderStatusRequest("DONE", null);

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime bookedTime = LocalDateTime.of(2023, Month.JANUARY, 25, 12, 10);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);

        OrderEntity oldData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.BOOKED, bookedTime, null, null, null, null, null, null, createdDate, modifiedDate, 0.0);
        OrderEntity expectedData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.DONE, bookedTime, null, null, null, bookedTime.plusHours(2), bookedTime.plusHours(3), null, createdDate, modifiedDate, 0.0);

        // WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(oldData));
        when(orderRepository.save(oldData)).thenReturn(expectedData);

        OrderResponse updatedStatusOrder = orderService.updateStatus(orderId, request);

        // THEN
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));

        assertEquals(OrderStatus.DONE, updatedStatusOrder.getStatus());
    }

    @Test
    void update_order_status_rejected() {
        // GIVEN
        UUID orderId = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75");

        OrderStatusRequest request = new OrderStatusRequest("REJECTED", "Bengkel tidak melayani kendaraan tipe listrik");

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime bookedTime = LocalDateTime.of(2023, Month.JANUARY, 25, 12, 10);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);

        OrderEntity oldData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.BOOKED, bookedTime, null, null, null, null, null, null, createdDate, modifiedDate, 0.0);
        OrderEntity expectedData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, "Bengkel tidak melayani kendaraan tipe listrik", OrderStatus.REJECTED, bookedTime, null, null, null, null, null, bookedTime.plusMinutes(30), createdDate, modifiedDate, 0.0);

        // WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(oldData));
        when(orderRepository.save(oldData)).thenReturn(expectedData);

        OrderResponse updatedStatusOrder = orderService.updateStatus(orderId, request);

        // THEN
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));

        assertEquals(OrderStatus.REJECTED, updatedStatusOrder.getStatus());
    }

    @Test
    void update_order_status_rejected_but_rejection_note_is_empty() {
        // GIVEN
        UUID orderId = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75");

        // Rejection Note is null or empty
        OrderStatusRequest request = new OrderStatusRequest("REJECTED", null);

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime bookedTime = LocalDateTime.of(2023, Month.JANUARY, 25, 12, 10);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);

        OrderEntity oldData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.BOOKED, bookedTime, null, null, null, null, null, null, createdDate, modifiedDate, 0.0);

        // WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(oldData));

        // THEN
        assertThrows(RejectionNoteEmptyException.class, () -> {
            orderService.updateStatus(orderId, request);
            verify(orderRepository, times(1)).findById(orderId);
        }, "RejectionNoteEmptyException was expected");
    }

    @Test
    void update_order_invalid_status() {
        // GIVEN
        UUID orderId = UUID.fromString("d1d25ee5-43a0-4ce6-a676-38b634be6d75");

        // Invalid Status (typo)
        OrderStatusRequest request = new OrderStatusRequest("ACCEPTD", null);

        LocalDateTime createdDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime modifiedDate = LocalDateTime.of(2023, Month.JANUARY, 19, 10, 10);
        LocalDateTime bookedTime = LocalDateTime.of(2023, Month.JANUARY, 25, 12, 10);

        UserEntity customer = new UserEntity(1, "John Doe", "john.doe@example.com", "0814412389", "@Adxfc66", "das", UserRole.CUSTOMER, false, createdDate, modifiedDate);
        UserEntity workshopOwner = new UserEntity(2, "Zorias Alto", "zorias.alto@example.com", "0814412389", "@Adxfc66", "das", UserRole.WORKSHOP, false, createdDate, modifiedDate);
        WorkshopEntity workshop = new WorkshopEntity(1, workshopOwner, "AHASS ALTO", "Desc AHASS ALTO", "Jakarta", 12310.0, 12312310.0, true, true, LocalTime.of(8, 30), LocalTime.of(16, 30), null, null, createdDate, modifiedDate);

        OrderEntity oldData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.BOOKED, bookedTime, null, null, null, null, null, null, createdDate, modifiedDate, 0.0);
//        OrderEntity expectedData = new OrderEntity(orderId, customer, workshop, null, "ORDER-21312", LocalDate.of(2023, 1, 19), LocalTime.of(8, 30), VehicleType.MOTORCYCLE, "B 1234 ABA", null, null, null, OrderStatus.ACCEPTED, bookedTime, null, bookedTime.plusMinutes(30), null, null, null, bookedTime.plusHours(1), createdDate, modifiedDate, 0.0);

        // WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(oldData));

        // THEN
        assertThrows(InvalidStatusParamException.class, () -> {
            orderService.updateStatus(orderId, request);
            verify(orderRepository, times(1)).findById(orderId);
        }, "InvalidStatusParamException was expected");
    }

}
