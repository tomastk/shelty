package tomastk.shelty.models.daos;


import org.springframework.data.jpa.repository.JpaRepository;
import tomastk.shelty.models.entities.UserData;

public interface UserDataDAO extends JpaRepository<UserData, Long> {
}
