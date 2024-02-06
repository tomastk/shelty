package tomastk.shelty.models.dtos;

import lombok.*;
import tomastk.shelty.models.entities.Animal;
import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Setter
@ToString
@Builder

public class RefugioDTO implements Serializable {
    private long id;
    private String nombre;
    private String imgUrl;
    private List<Animal> animales;
}
