package academy.doku.da3duawebserviceapi.mekaniku.workshop.repository;

import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.ProductType;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface WorkshopProductRepository extends JpaRepository<WorkshopProductEntity, UUID> {
    Page<WorkshopProductEntity> findAllByWorkshopIdAndNameContainingIgnoreCaseAndType(Integer workshopId, String query, ProductType type, Pageable pageable);

    Page<WorkshopProductEntity> findAllByWorkshopIdAndType(Integer workshopId, ProductType type, Pageable pageable);

    Page<WorkshopProductEntity> findAllByWorkshopIdAndNameContainingIgnoreCase(Integer workshopId, String query, Pageable pageable);

    Page<WorkshopProductEntity> findAllByWorkshopId(Integer workshopId, Pageable pageable);
}


