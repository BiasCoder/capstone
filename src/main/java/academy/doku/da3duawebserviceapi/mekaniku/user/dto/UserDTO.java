package academy.doku.da3duawebserviceapi.mekaniku.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.security.SecureRandom;

@AllArgsConstructor
@Data
@Builder
@ToString
public class UserDTO {
    private Integer id;
    private String fullname;
    private String email;
    private String phone;
    private String password;
    private String role;
    private Boolean suspend;

    public UserDTO(){
        this.suspend = false;
        this.password = generateRandomPassword();
    }

    public String generateRandomPassword() {
        String upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerChars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specials = "!@#$%^&*()_+-=[]{}|;':\"<>,.?/";

        String allChars = upperChars + lowerChars + numbers + specials;

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        sb.append(upperChars.charAt(random.nextInt(upperChars.length())));
        sb.append(lowerChars.charAt(random.nextInt(lowerChars.length())));
        sb.append(numbers.charAt(random.nextInt(numbers.length())));
        sb.append(specials.charAt(random.nextInt(specials.length())));

        int remainingLength = 4;
        for (int i = 0; i < remainingLength; i++) {
            int randomIndex = random.nextInt(allChars.length());
            sb.append(allChars.charAt(randomIndex));
        }
        return sb.toString();
    }
}
