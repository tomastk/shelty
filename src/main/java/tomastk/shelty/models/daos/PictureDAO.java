package tomastk.shelty.models.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tomastk.shelty.models.entities.Picture;

@Repository
public interface PictureDAO extends JpaRepository<Picture, Long> {
}
