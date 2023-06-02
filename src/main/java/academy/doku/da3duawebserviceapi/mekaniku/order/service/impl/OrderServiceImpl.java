package academy.doku.da3duawebserviceapi.mekaniku.order.service.impl;


import academy.doku.da3duawebserviceapi.common.dto.PaginationResponse;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.OrderResponse;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.OrderStatusRequest;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.UpdateOrderDetailRequest;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.UpdateOrderRequest;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.OrderDetailEntity;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.OrderEntity;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.enums.OrderStatus;
import academy.doku.da3duawebserviceapi.mekaniku.order.exception.InvalidStatusParamException;
import academy.doku.da3duawebserviceapi.mekaniku.order.exception.OrderNotFoundException;
import academy.doku.da3duawebserviceapi.mekaniku.order.exception.RejectionNoteEmptyException;
import academy.doku.da3duawebserviceapi.mekaniku.order.repository.OrderDetailRepository;
import academy.doku.da3duawebserviceapi.mekaniku.order.repository.OrderRepository;
import academy.doku.da3duawebserviceapi.mekaniku.order.service.GetMerchantOrderByIdService;
import academy.doku.da3duawebserviceapi.mekaniku.order.service.GetMerchantOrderListService;
import academy.doku.da3duawebserviceapi.mekaniku.order.service.UpdateOrderService;
import academy.doku.da3duawebserviceapi.mekaniku.order.service.UpdateOrderStatusService;
import academy.doku.da3duawebserviceapi.mekaniku.report.dto.PaymentReportResponse;
import academy.doku.da3duawebserviceapi.mekaniku.report.entity.PaymentEntity;
import academy.doku.da3duawebserviceapi.mekaniku.report.entity.enums.PaymentStatus;
import academy.doku.da3duawebserviceapi.mekaniku.report.repository.PaymentRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopProductEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.repository.WorkshopProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements GetMerchantOrderListService, GetMerchantOrderByIdService, UpdateOrderStatusService, UpdateOrderService {

    private final ModelMapper modelMapper;

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final WorkshopProductRepository workshopProductRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public PaginationResponse getMerchantOrderList(Pageable pageable, Integer workshopId, String status) {

        OrderStatus orderStatus = defineOrderStatus(status);

        Page<OrderEntity> merchantOrders = orderRepository.findAllByWorkshopIdAndStatusOrderByCreatedAtDesc(pageable, workshopId, orderStatus);
        List<OrderResponse> orderList = merchantOrders.stream().map(orderEntity -> {

            OrderResponse mappedOrder = modelMapper.map(orderEntity, OrderResponse.class);

            // Set invoice by payment
            if (mappedOrder.getStatus().equals(OrderStatus.DONE)) {
                Optional<PaymentEntity> payment = paymentRepository.findByOrderIdAndStatus(orderEntity.getId(), PaymentStatus.SUCCESS);

                if (payment.isPresent()) {
                    PaymentReportResponse invoice = new PaymentReportResponse();
                    invoice.setPaymentName(payment.get().getPaymentMethod().getDescription());
                    invoice.setPaymentImageUrl(payment.get().getPaymentMethod().getImageUrl());

                    mappedOrder.setInvoiceNumber(payment.get().getInvoiceNumber());
                    mappedOrder.setInvoice(invoice);
                }
            }

            // Set total price
            mappedOrder.setTotalPrice("0");
            if (orderEntity.getTotalPrice() != null) {
                mappedOrder.setTotalPrice(BigDecimal.valueOf(orderEntity.getTotalPrice()).toPlainString());
            }

            return mappedOrder;

        }).toList();

        return PaginationResponse.builder()
                .totalItem(merchantOrders.getTotalElements())
                .totalPage(merchantOrders.getTotalPages())
                .currentPage(merchantOrders.getNumber() + 1)
                .list(orderList)
                .build();
    }

    @Override
    public OrderResponse getMerchantOrder(UUID orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order with ID: " + orderId + " not found")
        );

        OrderResponse mappedOrder = modelMapper.map(order, OrderResponse.class);

        // Set invoice by payment
        if (mappedOrder.getStatus().equals(OrderStatus.DONE)) {
            Optional<PaymentEntity> payment = paymentRepository.findByOrderIdAndStatus(order.getId(), PaymentStatus.SUCCESS);

            if (payment.isPresent()) {
                PaymentReportResponse invoice = new PaymentReportResponse();
                invoice.setPaymentName(payment.get().getPaymentMethod().getDescription());
                invoice.setPaymentImageUrl(payment.get().getPaymentMethod().getImageUrl());

                mappedOrder.setInvoiceNumber(payment.get().getInvoiceNumber());
                mappedOrder.setInvoice(invoice);
            }
        }

        // Set total price
        mappedOrder.setTotalPrice("0");
        if (order.getTotalPrice() != null) {
            mappedOrder.setTotalPrice(BigDecimal.valueOf(order.getTotalPrice()).toPlainString());
        }

        return mappedOrder;
    }

    @Override
    public OrderResponse updateStatus(UUID orderId, OrderStatusRequest request) {

        OrderStatus orderStatus = defineOrderStatus(request.getStatus());

        OrderEntity order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(orderStatus);

        switch (orderStatus) {
            case CANCELED -> order.setCanceledAt(LocalDateTime.now());
            case ACCEPTED -> order.setAcceptedAt(LocalDateTime.now());
            case IN_PROCESS -> order.setInProcessAt(LocalDateTime.now());
            case PROCESSED -> order.setProcessedAt(LocalDateTime.now());
            case DONE -> order.setDoneAt(LocalDateTime.now());
            case REJECTED -> {
                if (request.getRejectionNote() == null || request.getRejectionNote().isBlank() || request.getRejectionNote().isEmpty()) {
                    throw new RejectionNoteEmptyException("Rejection Note is empty");
                }
                order.setRejectionNote(request.getRejectionNote());
                order.setRejectedAt(LocalDateTime.now());
            }
            default -> throw new InvalidStatusParamException("Status is invalid");
        }

        return modelMapper.map(orderRepository.save(order), OrderResponse.class);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(UUID orderId, UpdateOrderRequest request) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order with ID: " + orderId + " not found")
        );

        orderDetailRepository.deleteAllByOrderId(orderId);

        Set<OrderDetailEntity> orderDetails = new HashSet<>();
        for (UpdateOrderDetailRequest productReq: request.getProducts()) {
            log.info("PRODUCT ID: " + productReq.getProductId());
            WorkshopProductEntity product = workshopProductRepository.findById(productReq.getProductId()).orElseThrow();
            orderDetails.add(new OrderDetailEntity(
                    order,
                    product,
                    product.getName(),
                    product.getPrice(),
                    product.getType(),
                    productReq.getQuantity()));
        }

        order.setOrderDetails(orderDetails);
        order.setWorkshopNote(request.getWorkshopNote());
        orderRepository.save(order);

        return modelMapper.map(order, OrderResponse.class);
    }

    private OrderStatus defineOrderStatus(String status) {
        return switch (status) {
            case "BOOKED" -> OrderStatus.BOOKED;
            case "CANCELED" -> OrderStatus.CANCELED;
            case "ACCEPTED" -> OrderStatus.ACCEPTED;
            case "IN_PROCESS" -> OrderStatus.IN_PROCESS;
            case "PROCESSED" -> OrderStatus.PROCESSED;
            case "DONE" -> OrderStatus.DONE;
            case "REJECTED" -> OrderStatus.REJECTED;
            default -> throw new InvalidStatusParamException("Status is invalid");
        };
    }
}
