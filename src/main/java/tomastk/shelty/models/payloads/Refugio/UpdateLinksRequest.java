package tomastk.shelty.models.payloads.Refugio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateLinksRequest {
    private String facebook;
    private String instagram;
    private String youtube;
    private String whatsapp;
    private String twitter;
    private String website;
    private String donationLink;
}
