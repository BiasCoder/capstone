package academy.doku.da3duawebserviceapi.mekaniku.workshop.dto;

import academy.doku.da3duawebserviceapi.mekaniku.workshop.entity.ProductType;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class EditProductDTO {
    UUID id;
    String name;
    Double price;
    ProductType type;
}
