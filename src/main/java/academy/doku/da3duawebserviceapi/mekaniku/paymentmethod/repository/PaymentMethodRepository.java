package academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.repository;

import academy.doku.da3duawebserviceapi.mekaniku.paymentmethod.entity.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Integer> {
    List<PaymentMethodEntity> findAllByOrderByIdAsc();

}
