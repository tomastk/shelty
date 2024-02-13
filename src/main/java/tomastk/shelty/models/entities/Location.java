package tomastk.shelty.models.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class Location {
    @Id
    private long id;
    private String provincia;
    private String pais;

    @OneToMany(mappedBy = "location")
    @JsonManagedReference
    List<Refugio> refugios;

}
