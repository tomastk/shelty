package tomastk.shelty.models.dtos;

import lombok.*;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class AnimalResponseDTO extends AnimalRequestDTO {
    private long id;
}