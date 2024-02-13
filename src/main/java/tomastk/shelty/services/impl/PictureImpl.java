package tomastk.shelty.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tomastk.shelty.models.daos.PictureDAO;
import tomastk.shelty.models.entities.Picture;
import tomastk.shelty.services.IPictureService;

@Service
public class PictureImpl implements IPictureService {

    @Autowired
    private PictureDAO pictureDAO;

    @Override
    public Picture save(Picture picture) {
        return pictureDAO.save(picture);
    }

    @Override
    public Picture getById(long id) {
        return pictureDAO.findById(id).orElse(null);
    }

    @Override
    public boolean existsById(long id) {
        return pictureDAO.existsById(id);
    }

    @Override
    public void delete(Picture picture) {
        pictureDAO.delete(picture);
    }
}
