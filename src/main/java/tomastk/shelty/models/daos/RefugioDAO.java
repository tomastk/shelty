package tomastk.shelty.models.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import tomastk.shelty.models.entities.Refugio;

public interface RefugioDAO extends JpaRepository<Refugio, Long> {
}
