package academy.doku.da3duawebserviceapi.mekaniku.user.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class ProfilDTO {
    private Integer id;
    private String fullname;
    private String email;
    private String phone;
    private String role;
    private Integer workshopId;
}
