package academy.doku.da3duawebserviceapi.mekaniku.user.dto;

import lombok.*;

@AllArgsConstructor
@Data
@Builder
@ToString
@RequiredArgsConstructor
public class ChangePasswordDTO {
    String oldPassword;
    String newPassword;
}
