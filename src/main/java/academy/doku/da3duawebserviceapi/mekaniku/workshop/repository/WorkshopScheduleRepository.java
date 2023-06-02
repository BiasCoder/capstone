package academy.doku.da3duawebserviceapi.mekaniku.workshop.repository;

import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkshopScheduleRepository extends JpaRepository<WorkshopScheduleEntity, UUID> {
    List<WorkshopScheduleEntity> findAllByWorkshopId(Integer workshopId);

}
