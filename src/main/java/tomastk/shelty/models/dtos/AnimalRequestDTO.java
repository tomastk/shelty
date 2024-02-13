package tomastk.shelty.models.dtos;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AnimalRequestDTO implements Serializable {
    private String nombre;
    private int especie_id;
    private String img_url;
    private long refugio_id;
}