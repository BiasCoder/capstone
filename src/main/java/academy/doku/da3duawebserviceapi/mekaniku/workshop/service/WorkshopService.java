package academy.doku.da3duawebserviceapi.mekaniku.workshop.service;

import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.*;
import org.springframework.data.domain.Pageable;


public interface WorkshopService {
    WorkshopDTO createMerchant(Integer userId, WorkshopDTO workshopDTO);

    WorkshopDetailDTO getById(Integer id);

    PaginateWorkshopDTO getAllWorkshopWithPagination (Pageable pageable);

    WorkshopDetailDTO findByUserId(Integer userId);

    UpdateProfilWorkshopDTO updateWorkshop (UpdateProfilWorkshopDTO updateProfilWorkshopDTO, Integer id);

    SuspendDTO changeStatus (SuspendDTO suspendDTO, Integer id);

    DescDTO changeDesc(DescDTO descDTO, Integer id);

}
