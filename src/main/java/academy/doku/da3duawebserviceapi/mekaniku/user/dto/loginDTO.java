package academy.doku.da3duawebserviceapi.mekaniku.user.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class loginDTO {
    private String token;
    private String role;
}
