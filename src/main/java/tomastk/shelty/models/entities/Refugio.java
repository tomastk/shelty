package tomastk.shelty.models.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Excluir propiedades nulas
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "Refugios", uniqueConstraints = {@UniqueConstraint(columnNames = {"nombre"}) })


public class Refugio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nombre;

    @OneToOne
    @JoinColumn(name="picture_id")
    private Picture picture;

    @OneToOne
    @JoinColumn(name="social_links_id")
    private SocialLinks social_links;

    private String main_link;

    @ManyToOne
    @JoinColumn(name="location_id")
    private Location location;


    private String short_description;

    private String donation_link;

    private String maps_data;

    private String historia;

    private String youtubevideo;

    @ManyToOne
    @JoinColumn(name="main_animal_id")
    private Animal mainAnimal;

    @OneToMany(mappedBy = "refugio", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Animal> animales;

    @Column(name="creator_id")
    private long creatorID;

    @ElementCollection
    private List<Long> administradores;
}
