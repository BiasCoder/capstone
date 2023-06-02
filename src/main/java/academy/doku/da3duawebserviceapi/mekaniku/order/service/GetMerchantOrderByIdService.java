package academy.doku.da3duawebserviceapi.mekaniku.order.service;

import academy.doku.da3duawebserviceapi.mekaniku.order.dto.OrderResponse;

import java.util.UUID;

public interface GetMerchantOrderByIdService {
    OrderResponse getMerchantOrder(UUID orderId);
}
