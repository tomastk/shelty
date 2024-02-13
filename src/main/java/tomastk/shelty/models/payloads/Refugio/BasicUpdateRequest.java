package tomastk.shelty.models.payloads.Refugio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasicUpdateRequest {
    private String nombre;
    private String shortDescription;
    private String historia;
    private String youtubeData;
}
