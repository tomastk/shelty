package tomastk.shelty.services.impl;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tomastk.shelty.models.daos.AnimalDAO;
import tomastk.shelty.models.entities.Animal;
import tomastk.shelty.services.IAnimalService;

import java.util.List;

@Service
public class AnimalImpl implements IAnimalService {

    @Autowired
    private AnimalDAO animalDAO;

    @Override
    public Animal save(Animal animal) {
        return animalDAO.save(animal);
    }

    @Override
    public Animal findById(long id) {
        return animalDAO.findById(id).orElse(null);
    }

    @Override
    public void delete(Animal animal) {
        animalDAO.delete(animal);
    }

    @Override
    public List<Animal> getAll() {
        return animalDAO.findAll();
    }

    @Override
    public boolean existsById(long id) {
        return animalDAO.existsById(id);
    }

    @Override
    public Page<Animal> getByEspecie(long id, Pageable pageable) {
        return animalDAO.getAnimalByEspecie(id, pageable);
    }

    @Override
    public Page<Animal> findAll(Pageable pageable) {
        Page<Animal> animales = animalDAO.findAll(pageable);
        return animales;
    }

}
