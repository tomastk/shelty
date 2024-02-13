package tomastk.shelty.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SocialLinks {
    @Id
    private long id;

    private String whatsapp;
    private String instagram;
    private String twitter;
    private String facebook;
    private String youtube;

}
