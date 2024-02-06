package tomastk.shelty.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tomastk.shelty.models.daos.EspecieDAO;
import tomastk.shelty.models.entities.Especie;
import tomastk.shelty.services.IEspecieService;

import java.util.List;
import java.util.Optional;

@Service
public class EspecieImpl implements IEspecieService {
    @Autowired
    private EspecieDAO especieDAO;
    @Override
    public Especie save(Especie data) {
        return especieDAO.save(data);
    }
    @Override
    public Especie findById(long id) {
        return especieDAO.findById(id).orElse(null);
    }
    @Override
    public boolean existsById(long id) {
        return especieDAO.existsById(id);
    }

    @Override
    public Especie deleteById(long id) {
        Optional<Especie> especieBorrar = especieDAO.findById(id);

        if (especieBorrar.isPresent()) {
            especieDAO.delete(especieBorrar.get());
            return especieBorrar.get();
        } else {
            return null;
        }

    }

    @Override
    public List<Especie> getAll() {
        return especieDAO.findAll();
    }
}
