package academy.doku.da3duawebserviceapi.mekaniku.workshop.repository;

import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkshopPhotoRepository extends JpaRepository<WorkshopPhotoEntity, UUID> {
    List<WorkshopPhotoEntity> findByWorkshopId(Integer workshopId);
}
