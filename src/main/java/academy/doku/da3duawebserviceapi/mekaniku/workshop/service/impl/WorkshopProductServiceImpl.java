package academy.doku.da3duawebserviceapi.mekaniku.workshop.service.impl;

import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.EditProductDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.PaginateProductDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.ProductDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.ResponseDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.ProductType;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.WorkshopProductEntity;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.repository.WorkshopProductRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.repository.WorkshopRepository;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.WorkshopProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Log4j2
public class WorkshopProductServiceImpl implements WorkshopProductService {

    private final WorkshopProductRepository workshopProductRepository;

    private final ModelMapper modelMapper;
    private final WorkshopRepository workshopRepository;

    @Override
    public ProductDTO addProduct(Integer id, ProductDTO productDTO) {
        WorkshopEntity workshop = workshopRepository.findById(id)
                .orElseThrow();

        WorkshopProductEntity workshopProduct = modelMapper.map(productDTO, WorkshopProductEntity.class);
        workshopProduct.setWorkshop(workshop);
        workshopProduct.setType(ProductType.valueOf(String.valueOf(productDTO.getType()))); // konversi tipe data


        workshopProduct = workshopProductRepository.save(workshopProduct);
        productDTO.setId(workshopProduct.getId());

        return productDTO;
    }

    @Override
    public EditProductDTO editProduct(UUID id, EditProductDTO editProductDTO) {
        Optional<WorkshopProductEntity> optionalWorkshopProductEntity = workshopProductRepository.findById(id);
        if (!optionalWorkshopProductEntity.isPresent()){
            throw new RuntimeException("Not Found");
        }
        WorkshopProductEntity workshopProduct = optionalWorkshopProductEntity.get();
        workshopProduct.setName(editProductDTO.getName());
        workshopProduct.setPrice(editProductDTO.getPrice());

        workshopProductRepository.save(workshopProduct);
        editProductDTO.setId(workshopProduct.getId());
        editProductDTO.setType(workshopProduct.getType());

        return editProductDTO;
    }

    @Override
    public ResponseDTO deleteProduct(UUID id, ResponseDTO responseDTO) {
        workshopProductRepository.deleteById(id);
        return responseDTO;
    }

    @Override
    public ResponseDTO deleteMultiProduct(List<UUID> ids) {
        workshopProductRepository.deleteAllById(ids);
        return ResponseDTO.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Products Deleted")
                .data(ids)
                .build();
    }

    @Override
    public PaginateProductDTO getAllProducts(Integer workshopId , String query, ProductType type, Pageable pageable) {
        Page<WorkshopProductEntity> products;
        if (StringUtils.hasText(query) && type != null) {
            products = workshopProductRepository.findAllByWorkshopIdAndNameContainingIgnoreCaseAndType(workshopId, query, type, pageable);
        } else if (type != null) {
            products = workshopProductRepository.findAllByWorkshopIdAndType(workshopId, type, pageable);
        } else if (StringUtils.hasText(query)) {
            products = workshopProductRepository.findAllByWorkshopIdAndNameContainingIgnoreCase(workshopId, query, pageable);
        } else {
            products = workshopProductRepository.findAllByWorkshopId(workshopId, pageable);
        }

        return convertToPageDTO(products);
    }


    private PaginateProductDTO convertToPageDTO(Page<WorkshopProductEntity> products) {
        PaginateProductDTO paginateProductDTO = new PaginateProductDTO();
        paginateProductDTO.setTotalItems(products.getTotalElements());
        paginateProductDTO.setCurrentPage(products.getNumber()+1);
        paginateProductDTO.setTotalPage(products.getTotalPages());

        List<ProductDTO> List = products.stream()
                .map(product -> {
                    log.info("Product ID: "+product.getId());
                    return new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getType());
                })
                .collect(Collectors.toList());

        paginateProductDTO.setList(List);

        return paginateProductDTO;
    }


}
