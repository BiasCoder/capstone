package academy.doku.da3duawebserviceapi.mekaniku.workshop.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class UserContactDTO {
    private Integer id;
    private String phone;
    private String email;
    private Boolean suspend;
    private String avatarUrl;

    // constructor, getters, and setters
}




