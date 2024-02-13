package tomastk.shelty.models.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import tomastk.shelty.models.animalEnums.Comportamiento;
import tomastk.shelty.models.animalEnums.Especie;
import tomastk.shelty.models.animalEnums.Genero;
import tomastk.shelty.models.animalEnums.Size;

import java.io.Serializable;


@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Excluir propiedades nulas
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "Animal")
public class Animal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Enumerated(EnumType.STRING)
    private Especie especie;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "refugio_id")
    @JsonBackReference
    private Refugio refugio;

    @OneToOne
    @JoinColumn(name="picture_id")
    private Picture picture;

    @Enumerated(EnumType.STRING)
    private Size size;

    @Enumerated(EnumType.STRING)
    private Comportamiento comportamientoConAnimales;

    @Enumerated(EnumType.STRING)
    private Comportamiento comportamientoConPersonas;

    boolean desparasitado;

    String enfermedad;
    String cuidadosEspeciales;
    String cuidadosExtra;

    String longDescription;

    private long ownerId;
}