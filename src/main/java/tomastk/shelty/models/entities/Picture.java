package tomastk.shelty.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Picture {
    @Id
    long id;
    @Column(name = "profile_picture", nullable = false)
    String profilePicture;
    @Column(name = "main_picture")
    String mainPicture;
    @Column(name = "secondary_picture")
    String secondaryPicture;
}
