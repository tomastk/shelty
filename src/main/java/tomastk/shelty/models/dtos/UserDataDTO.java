package tomastk.shelty.models.dtos;
import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class UserDataDTO implements Serializable {
    private String description;
}
