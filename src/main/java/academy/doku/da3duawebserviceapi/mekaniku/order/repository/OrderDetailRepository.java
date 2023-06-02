package academy.doku.da3duawebserviceapi.mekaniku.order.repository;

import academy.doku.da3duawebserviceapi.mekaniku.order.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, UUID> {

    void deleteAllByOrderId(UUID orderId);


}
