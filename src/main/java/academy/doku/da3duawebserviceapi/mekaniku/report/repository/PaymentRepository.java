package academy.doku.da3duawebserviceapi.mekaniku.report.repository;

import academy.doku.da3duawebserviceapi.mekaniku.report.entity.PaymentEntity;

import academy.doku.da3duawebserviceapi.mekaniku.report.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
    Optional<PaymentEntity> findByOrderIdAndStatus(UUID orderId, PaymentStatus status);
}
