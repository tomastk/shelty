package tomastk.shelty.models.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tomastk.shelty.models.entities.Location;

@Repository
public interface LocationDAO extends JpaRepository<Location, Long> {
}
