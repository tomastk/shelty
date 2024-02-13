package tomastk.shelty.models.payloads;

import lombok.*;
import tomastk.shelty.models.animalEnums.Especie;

import java.io.Serializable;

@Data
@Getter
@Setter
@ToString
@Builder

public class CreateAnimalRequest implements Serializable {
    private String nombre;
    private Especie especie;
    private String imgUrl;
}