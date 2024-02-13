package tomastk.shelty.models.payloads.Refugio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePictureRequest {
    private String profilePictureUrl;
    private String mainPictureUrl;
    private String secondaryPictureUrl;
}
