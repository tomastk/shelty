package tomastk.shelty.models.dtos;

import lombok.*;

@Data
@Builder

public class EspecieDTO {
    private String nombre;
    private String descripcion;
    private String img_url;
}
