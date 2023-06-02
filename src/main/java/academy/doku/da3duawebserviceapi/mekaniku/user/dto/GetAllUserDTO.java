package academy.doku.da3duawebserviceapi.mekaniku.user.dto;


import lombok.*;

@AllArgsConstructor
@Data
@Builder
@ToString
@NoArgsConstructor
public class GetAllUserDTO {
    Integer id;
    String fullname;
    String email;
    String phone;
    String role;
    Boolean suspend;
}
