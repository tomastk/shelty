package tomastk.shelty.services;

import tomastk.shelty.models.entities.Especie;
import tomastk.shelty.models.entities.UserData;

import java.util.List;

public interface IEspecieService {
    Especie save(Especie data);
    Especie findById(long id);
    boolean existsById(long id);

    Especie deleteById(long id);

    List<Especie> getAll();

}
