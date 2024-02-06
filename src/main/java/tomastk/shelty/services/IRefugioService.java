package tomastk.shelty.services;

import tomastk.shelty.models.entities.Refugio;

import java.util.List;


public interface IRefugioService {
    Refugio save(Refugio refugio);
    Refugio findById(long id);
    void delete(Refugio refugio);
    List<Refugio> getAll();
    boolean existsById(long id);
}