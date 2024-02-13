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
    private String imgUrl;

    @OneToMany(mappedBy = "refugio", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Animal> animales;

    @Column(name="creator_id")
    private long creatorID;

    @ElementCollection
    private List<Long> administradores;
}
