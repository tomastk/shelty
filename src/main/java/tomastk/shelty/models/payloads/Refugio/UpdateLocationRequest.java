package tomastk.shelty.models.payloads.Refugio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateLocationRequest {
    private String pais;
    private String provincia;
    private String mapsData;
}
