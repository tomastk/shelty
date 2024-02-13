package tomastk.shelty.models.dtos;

import lombok.*;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimalDetailDTO {
    private long id;
    private String nombre;
    private String img_url;
    private String descripcion;
    private String especie;
}
