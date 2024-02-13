package tomastk.shelty.services;

import tomastk.shelty.models.dtos.RefugioDTO;
import tomastk.shelty.models.entities.Location;
import tomastk.shelty.models.entities.Picture;
import tomastk.shelty.models.entities.Refugio;
import tomastk.shelty.models.entities.SocialLinks;

import java.sql.Ref;
import java.util.List;


public interface IRefugioService {
    Refugio save(Refugio refugio);
    Refugio findById(long id);
    void delete(Refugio refugio);
    List<Refugio> getAll();
    boolean existsById(long id);

    List<Refugio> getByUser(long id);
    Refugio updateLinks(SocialLinks socialLinks, Refugio refugioToUpdate);
    Refugio updateLocation(Location location, Refugio refugioToUpdate);
    Refugio updatePicture(Picture picture, Refugio refugioToUpdate);
}