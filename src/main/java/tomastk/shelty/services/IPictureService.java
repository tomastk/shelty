package tomastk.shelty.services;

import tomastk.shelty.models.entities.Picture;

public interface IPictureService {
    Picture save(Picture picture);
    Picture getById(long id);
    boolean existsById(long id);
    void delete(Picture picture);
}
