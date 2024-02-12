package tomastk.shelty.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tomastk.shelty.models.daos.RefugioDAO;
import tomastk.shelty.models.dtos.RefugioDTO;
import tomastk.shelty.models.entities.Refugio;
import tomastk.shelty.services.IRefugioService;

import java.util.List;

@Service
public class RefugioImpl implements IRefugioService {

    @Autowired
    private RefugioDAO refugioDAO;

    @Override
    public Refugio save(Refugio refugio) {
        return refugioDAO.save(refugio);
    }

    @Override
    public Refugio findById(long id) {
        return refugioDAO.findById(id).orElse(null);
    }

    @Override
    public void delete(Refugio refugio) {
        refugioDAO.delete(refugio);
    }

    @Override
    public List<Refugio> getAll() {
        return refugioDAO.findAll();
    }

    @Override
    public boolean existsById(long id) {
        return refugioDAO.existsById(id);
    }

    public List<Refugio> getByUser(long id) {
        return refugioDAO.getByUserId(id);
    }
}
