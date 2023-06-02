package academy.doku.da3duawebserviceapi.mekaniku.order.service;

import academy.doku.da3duawebserviceapi.mekaniku.order.dto.OrderResponse;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.OrderStatusRequest;

import java.util.UUID;

public interface UpdateOrderStatusService {
    OrderResponse updateStatus(UUID orderId, OrderStatusRequest request);
}

