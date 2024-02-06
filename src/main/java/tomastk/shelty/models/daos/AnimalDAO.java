package tomastk.shelty.models.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tomastk.shelty.models.entities.Animal;

import java.util.List;

public interface AnimalDAO extends JpaRepository<Animal, Long> {
    @Query("SELECT a FROM Animal a WHERE a.especie.id = :especieId")
    Page<Animal> getAnimalByEspecie(@Param("especieId") long especieId, Pageable pageable);
}
