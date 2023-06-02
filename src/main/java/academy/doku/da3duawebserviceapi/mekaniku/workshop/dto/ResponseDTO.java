package academy.doku.da3duawebserviceapi.mekaniku.workshop.dto;


import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class ResponseDTO<T> implements Serializable {
    private int statusCode;
    private String message;
    private T data;
}
