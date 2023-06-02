package academy.doku.da3duawebserviceapi.mekaniku.order.service;

import academy.doku.da3duawebserviceapi.mekaniku.order.dto.OrderResponse;
import academy.doku.da3duawebserviceapi.mekaniku.order.dto.UpdateOrderRequest;

import java.util.UUID;

public interface UpdateOrderService {

    OrderResponse updateOrder(UUID orderId, UpdateOrderRequest request);
}
