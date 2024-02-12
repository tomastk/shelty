package tomastk.shelty.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "user_codes")
public class UserCode {
    Date generatedAt;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    String code;
}
