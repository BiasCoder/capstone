package academy.doku.da3duawebserviceapi.mekaniku.order.service;

import academy.doku.da3duawebserviceapi.common.dto.PaginationResponse;
import org.springframework.data.domain.Pageable;

public interface GetMerchantOrderListService {

    PaginationResponse getMerchantOrderList(Pageable pageable, Integer workshopId, String status);
}
