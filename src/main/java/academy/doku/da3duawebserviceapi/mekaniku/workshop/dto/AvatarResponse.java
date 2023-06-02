package academy.doku.da3duawebserviceapi.mekaniku.workshop.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class AvatarResponse {
    private Integer id;
    private String avatar_url;
}
