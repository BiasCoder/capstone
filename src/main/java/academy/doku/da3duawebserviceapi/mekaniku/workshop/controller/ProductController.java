package academy.doku.da3duawebserviceapi.mekaniku.workshop.controller;

import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.EditProductDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.PaginateProductDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.ProductDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.dto.ResponseDTO;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.ProductType;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.WorkshopProductService;
import academy.doku.da3duawebserviceapi.mekaniku.workshop.service.WorkshopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {


    private final WorkshopProductService workshopProductService;

    private final WorkshopService workshopService;

    @PostMapping
    public ResponseEntity<ResponseDTO> addProduct(Principal principal, @RequestBody ProductDTO productDTO){
        Integer userId = Integer.valueOf(principal.getName());
        Integer id = workshopService.findByUserId(userId).getId();
        workshopProductService.addProduct(id, productDTO);
        return new ResponseEntity<>(ResponseDTO.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Product Created")
                .data(productDTO)
                .build(), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO<EditProductDTO>> editProduct(@PathVariable UUID id, @RequestBody EditProductDTO editProductDTO){
        workshopProductService.editProduct(id, editProductDTO);
        return new ResponseEntity<>(ResponseDTO.<EditProductDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Product Updated")
                .data(editProductDTO)
                .build(), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteProduct (@PathVariable UUID id){
        workshopProductService.deleteProduct(id, null);
        return new ResponseEntity<ResponseDTO>(ResponseDTO.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Product Deleted")
                .data(null)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/delete-products")
    public ResponseEntity<ResponseDTO> deleteProducts(@RequestBody List<UUID> ids) {
        ResponseDTO responseDTO = workshopProductService.deleteMultiProduct(ids);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<PaginateProductDTO>> getAllProducts(@RequestParam(required = false) String query,
                                                                          @RequestParam(required = false) ProductType type,
                                                                          @RequestParam(defaultValue = "10") int limit,
                                                                          @RequestParam(defaultValue = "0") int page, Principal principal) {
        Integer userId = Integer.valueOf(principal.getName());
        Integer workshopId = workshopService.findByUserId(userId).getId();
        Pageable pageable = PageRequest.of(page , limit);
        PaginateProductDTO products = workshopProductService.getAllProducts(workshopId, query, type, pageable);

        ResponseDTO<PaginateProductDTO> response = new ResponseDTO<>(HttpStatus.OK.value(), "success", products);
        return ResponseEntity.ok(response);
    }

}
