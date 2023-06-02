package academy.doku.da3duawebserviceapi.mekaniku.workshop.repository;


import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkshopRepository extends JpaRepository<WorkshopEntity, Integer> {
    Page <WorkshopEntity> findAll(Pageable pageable);
    Optional<WorkshopEntity> findByUserId(Integer userId);
}
