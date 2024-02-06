package tomastk.shelty.models.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import tomastk.shelty.models.entities.Especie;

public interface EspecieDAO extends JpaRepository<Especie, Long> {
}
