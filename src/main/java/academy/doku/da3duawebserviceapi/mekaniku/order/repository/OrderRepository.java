package academy.doku.da3duawebserviceapi.mekaniku.order.repository;

import academy.doku.da3duawebserviceapi.mekaniku.order.entity.OrderEntity;
import academy.doku.da3duawebserviceapi.mekaniku.order.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    Page<OrderEntity> findAllByWorkshopIdAndStatusOrderByCreatedAtDesc(Pageable pageable, Integer workshopId, OrderStatus status);

    Page<OrderEntity> findAllByWorkshopIdAndStatusIn(Pageable pageable, Integer workshopId, List<OrderStatus> statusList);
    List<OrderEntity> findAllByWorkshopIdAndStatusIn(Integer workshopId, List<OrderStatus> statusList);

    Page<OrderEntity> findAllByCustomerIdAndStatusIn(Pageable pageable, Integer customerId, List<OrderStatus> statusList);
    List<OrderEntity> findAllByCustomerIdAndStatusIn(Integer customerId, List<OrderStatus> statusList);

    // Report Service
    List<OrderEntity> findAllByWorkshopIdAndStatusInAndModifiedAtBetween(Integer workshopId, List<OrderStatus> status, LocalDateTime beforeDate, LocalDateTime currentDate);
    List<OrderEntity> findAllByCustomerIdAndStatusInAndModifiedAtBetween(Integer customerId, List<OrderStatus> status, LocalDateTime beforeDate, LocalDateTime currentDate);


}
