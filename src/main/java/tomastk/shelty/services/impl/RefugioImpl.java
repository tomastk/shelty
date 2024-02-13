package tomastk.shelty.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tomastk.shelty.models.daos.LocationDAO;
import tomastk.shelty.models.daos.RefugioDAO;
import tomastk.shelty.models.daos.SocialLinksDAO;
import tomastk.shelty.models.dtos.RefugioDTO;
import tomastk.shelty.models.entities.Location;
import tomastk.shelty.models.entities.Picture;
import tomastk.shelty.models.entities.Refugio;
import tomastk.shelty.models.entities.SocialLinks;
import tomastk.shelty.services.IRefugioService;

import java.util.List;

@Service
public class RefugioImpl implements IRefugioService {

    @Autowired
    private RefugioDAO refugioDAO;

    @Autowired
    private PictureImpl pictureService;
    @Autowired
    private SocialLinksDAO socialLinksDAO;
    @Autowired
    private LocationDAO locationDAO;

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

    @Override
    public Refugio updateLinks(SocialLinks socialLinks, Refugio refugioToUpdate) {
        SocialLinks socialLinksSaved = socialLinksDAO.save(socialLinks);
        refugioToUpdate.setSocial_links(socialLinksSaved);
        return refugioDAO.save(refugioToUpdate);
    }

    @Override
    public Refugio updateLocation(Location location, Refugio refugioToUpdate) {
        Location locationSaved = locationDAO.save(location);
        refugioToUpdate.setLocation(locationSaved);
        return refugioDAO.save(refugioToUpdate);
    }

    @Override
    public Refugio updatePicture(Picture picture, Refugio refugioToUpdate) {
        Picture pictureSaved = pictureService.save(picture);
        refugioToUpdate.setPicture(pictureSaved);
        return refugioDAO.save(refugioToUpdate);
    }
}
