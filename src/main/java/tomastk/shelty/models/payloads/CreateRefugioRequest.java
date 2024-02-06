package tomastk.shelty.models.payloads;

import lombok.*;
import java.io.Serializable;

@Data
@Getter
@Setter
@ToString
@Builder

public class CreateRefugioRequest implements Serializable {
    private String nombre;
    private String imgUrl;
}