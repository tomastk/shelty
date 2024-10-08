package tomastk.shelty.models.dtos;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class AnimalResponseDTO implements Serializable {
    private long id;
    private String nombre;
    private String especie;
    private String img_url;
    private String refugio;
}