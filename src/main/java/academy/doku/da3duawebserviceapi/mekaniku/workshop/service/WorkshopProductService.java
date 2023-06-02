package academy.doku.da3duawebserviceapi.mekaniku.workshop.service;

import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.EditProductDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.PaginateProductDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.ProductDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.ResponseDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.ProductType;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface WorkshopProductService {

    ProductDTO addProduct(Integer id, ProductDTO productDTO);

    EditProductDTO editProduct(UUID id, EditProductDTO editProductDTO);

    ResponseDTO deleteProduct(UUID id, ResponseDTO responseDTO);

    ResponseDTO deleteMultiProduct(List<UUID> ids);

    PaginateProductDTO getAllProducts(Integer workshopId, String query, ProductType type, Pageable pageable);

}
