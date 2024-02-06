package tomastk.shelty.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "UserData")
public class UserData {
    @Id
    private long id;
    private String description;
}
