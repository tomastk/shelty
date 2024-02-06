package tomastk.shelty.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tomastk.shelty.models.entities.Animal;

import java.util.List;


public interface IAnimalService {
    Animal save(Animal animal);
    Animal findById(long id);
    void delete(Animal animal);
    List<Animal> getAll();
    boolean existsById(long id);
    Page<Animal> getByEspecie(long id, Pageable pageable);
    Page<Animal> findAll(Pageable pageable);

}