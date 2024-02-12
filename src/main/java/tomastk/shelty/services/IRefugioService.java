package tomastk.shelty.services;

import tomastk.shelty.models.dtos.RefugioDTO;
import tomastk.shelty.models.entities.Refugio;

import java.util.List;


public interface IRefugioService {
    Refugio save(Refugio refugio);
    Refugio findById(long id);
    void delete(Refugio refugio);
    List<Refugio> getAll();
    boolean existsById(long id);

    List<Refugio> getByUser(long id);
}