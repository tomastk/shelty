package tomastk.shelty.controllers.Refugio;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tomastk.shelty.config.Messages;
import tomastk.shelty.models.entities.*;
import tomastk.shelty.models.payloads.Refugio.BasicUpdateRequest;
import tomastk.shelty.models.payloads.Refugio.UpdateLinksRequest;
import tomastk.shelty.models.payloads.Refugio.UpdateLocationRequest;
import tomastk.shelty.models.payloads.Refugio.UpdatePictureRequest;
import tomastk.shelty.services.impl.AdminSecurityContextHandler;
import tomastk.shelty.services.impl.AnimalImpl;
import tomastk.shelty.services.impl.RefugioImpl;
import tomastk.shelty.services.impl.ResponseImpl;
import tomastk.shelty.user.Role;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/refugio")
@CrossOrigin
public class UpdateRefugioController {
    @Autowired
    private ResponseImpl responseService;
    @Autowired
    private RefugioImpl service;
    @Autowired
    private AnimalImpl animalService;
    @Autowired
    private Logger logger;
    private final String ENTITY_NAME = "refugio";
    @PutMapping("/{id}/update-links")
    private ResponseEntity updateLinks(@PathVariable long id, @RequestBody UpdateLinksRequest request) {
        Refugio refugioToUpdate = service.findById(id);

        if (editionError(refugioToUpdate) != null) {
            return editionError(refugioToUpdate);
        }

        SocialLinks socialLinks = getSocialLinks(request, refugioToUpdate);
        refugioToUpdate.setDonation_link(request.getDonationLink());
        refugioToUpdate.setMain_link(request.getWebsite());

        try {
            Refugio refugioUpdated = service.updateLinks(socialLinks, refugioToUpdate);
            return responseService.sendSuccessResponse(
                    Messages.detailsSuccessEdition(ENTITY_NAME),
                    refugioUpdated,
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseService.sendErrorResponse(
                Map.of(Messages.editionError, Messages.detailEditionError(ENTITY_NAME)),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @PutMapping("/{id}/update-location")
    private ResponseEntity updateLocation(@PathVariable long id, @RequestBody UpdateLocationRequest request) {
        Refugio refugioToUpdate = service.findById(id);

        if (editionError(refugioToUpdate) != null) {
            return editionError(refugioToUpdate);
        }

        Location refugioLocation = getLocation(request, refugioToUpdate);
        refugioToUpdate.setMaps_data(request.getMapsData());

        try {
            Refugio refugioUpdated = service.updateLocation(refugioLocation, refugioToUpdate);
            return responseService.sendSuccessResponse(
                    Messages.detailsSuccessEdition(ENTITY_NAME),
                    refugioUpdated,
                    HttpStatus.CREATED
            );
        } catch (DataAccessException ex) {
            return responseService.sendErrorResponse(
                    Map.of(Messages.editionError, Messages.detailEditionError(ENTITY_NAME)),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @PutMapping("/{id}/update-main-animal")
    private ResponseEntity updateMainAnimal(@PathVariable long id, @RequestParam long animalId) {

        Refugio refugioToUpdate = service.findById(id);
        if (editionError(refugioToUpdate) != null) {
            return editionError(refugioToUpdate);
        }

        List<Animal> animals = refugioToUpdate.getAnimales();

        Animal refugioMainAnimal = animalService.findById(animalId);

        if (refugioMainAnimal == null) {
            return responseService.sendErrorResponse(
                    Map.of(Messages.nonFoundError, Messages.detailNotFoundError("animal")),
                    HttpStatus.NOT_FOUND
            );
        }

        if (!animals.contains(refugioMainAnimal)){
            return responseService.sendErrorResponse(
                    Map.of("message", "El animal no pertenece al refugio"),
                    HttpStatus.NOT_FOUND
            );
        }

        refugioToUpdate.setMainAnimal(refugioMainAnimal);

        try {
            Refugio refugioUpdated = service.save(refugioToUpdate);
            return responseService.sendSuccessResponse(
                    Messages.detailsSuccessEdition(ENTITY_NAME),
                    refugioUpdated,
                    HttpStatus.CREATED
            );
        } catch (DataAccessException ex) {
            return responseService.sendErrorResponse(
                    Map.of(Messages.editionError, Messages.detailEditionError(ENTITY_NAME)),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PutMapping("/{id}/update")
    private ResponseEntity update(@PathVariable long id, @RequestBody BasicUpdateRequest request) {
        Refugio refugioToUpdate = service.findById(id);
        if (editionError(refugioToUpdate) != null) {
            return editionError(refugioToUpdate);
        }
        refugioToUpdate.setShort_description(request.getShortDescription());
        refugioToUpdate.setHistoria(request.getHistoria());
        refugioToUpdate.setNombre(request.getNombre());
        refugioToUpdate.setYoutubevideo(request.getYoutubeData());

        try {
            Refugio refugioUpdated = service.save(refugioToUpdate);
            return responseService.sendSuccessResponse(
                    Messages.detailsSuccessEdition(ENTITY_NAME),
                    refugioUpdated,
                    HttpStatus.CREATED
            );
        } catch (DataAccessException ex) {
            return responseService.sendErrorResponse(
                    Map.of(Messages.editionError, Messages.detailEditionError(ENTITY_NAME)),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @PutMapping("/{id}/update-picture")
    private ResponseEntity updatePicture(@PathVariable long id, @RequestBody UpdatePictureRequest request) {

        Refugio refugioToUpdate = service.findById(id);
        if (editionError(refugioToUpdate) != null) {
            return editionError(refugioToUpdate);
        }

        Picture picture = getPicture(request, refugioToUpdate);

        try {
            Refugio refugioUpdated = service.updatePicture(picture, refugioToUpdate);
            return responseService.sendSuccessResponse(
                    Messages.detailsSuccessEdition(ENTITY_NAME),
                    refugioUpdated,
                    HttpStatus.CREATED
            );
        } catch (DataAccessException ex) {
            return responseService.sendErrorResponse(
                    Map.of(Messages.editionError, Messages.detailEditionError(ENTITY_NAME)),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    private ResponseEntity editionError(Refugio refugioToUpdate){

        if (refugioToUpdate == null) {
            return responseService.sendErrorResponse(
                    Map.of(Messages.nonFoundError, Messages.detailNotFoundError(ENTITY_NAME)),
                    HttpStatus.NOT_FOUND
            );
        }

        if (!userIsAuthorizated(refugioToUpdate)) {
            return responseService.sendErrorResponse(
                    Map.of(Messages.nonAuthorizatedError, Messages.detailNonAuthorizatedError(ENTITY_NAME)),
                    HttpStatus.UNAUTHORIZED
            );
        }

        return null;
    }

    private static Picture getPicture(UpdatePictureRequest request, Refugio refugioToUpdate) {
        Picture picture = refugioToUpdate.getPicture();
        if (picture == null) {
            picture = new Picture();
            picture.setId(refugioToUpdate.getId());
        }
        picture.setMainPicture(request.getMainPictureUrl());
        picture.setSecondaryPicture(request.getSecondaryPictureUrl());
        picture.setProfilePicture(request.getProfilePictureUrl());
        return picture;
    }

    private static Location getLocation(UpdateLocationRequest request, Refugio refugioToUpdate) {
        Location refugioLocation = refugioToUpdate.getLocation();
        if (refugioLocation == null) {
            refugioLocation = new Location();
            refugioLocation.setId(refugioToUpdate.getId());
        }
        refugioLocation.setProvincia(request.getProvincia());
        refugioLocation.setPais(request.getPais());
        return refugioLocation;
    }

    private static SocialLinks getSocialLinks(UpdateLinksRequest request, Refugio refugioToUpdate) {
        SocialLinks socialLinks = refugioToUpdate.getSocial_links();

        if (socialLinks == null) {
            socialLinks = new SocialLinks();
            socialLinks.setId(refugioToUpdate.getId());
        }

        socialLinks.setWhatsapp(request.getWhatsapp());
        socialLinks.setFacebook(request.getFacebook());
        socialLinks.setInstagram(request.getInstagram());
        socialLinks.setTwitter(request.getTwitter());
        socialLinks.setInstagram(request.getInstagram());
        socialLinks.setYoutube(request.getYoutube());
        return socialLinks;
    }

    private boolean userIsAuthorizated(Refugio refugio) {
        long userId = AdminSecurityContextHandler.getUserId();
        Role userRole = AdminSecurityContextHandler.getUserRole();
        return refugio.getAdministradores().contains(userId) || userRole == Role.ADMIN;
    }

}
