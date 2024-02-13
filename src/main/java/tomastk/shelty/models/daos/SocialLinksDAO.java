package tomastk.shelty.models.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tomastk.shelty.models.entities.SocialLinks;

@Repository
public interface SocialLinksDAO extends JpaRepository<SocialLinks, Long> {
}
