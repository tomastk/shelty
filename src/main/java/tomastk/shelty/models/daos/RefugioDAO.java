package tomastk.shelty.models.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tomastk.shelty.models.entities.Refugio;
import tomastk.shelty.models.dtos.RefugioDTO;
import java.util.List;

@Repository
public interface RefugioDAO extends JpaRepository<Refugio, Long> {


    @Query("SELECT r FROM Refugio r WHERE :userId MEMBER OF r.administradores")
    List<Refugio> getByUserId(long userId);

}
