package tomastk.shelty.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tomastk.shelty.config.Messages;
import tomastk.shelty.models.dtos.EspecieDTO;
import tomastk.shelty.models.entities.Especie;
import tomastk.shelty.models.payloads.MensajeResponse;
import tomastk.shelty.models.validators.EspecieValidator;
import tomastk.shelty.services.impl.EspecieImpl;
import tomastk.shelty.services.impl.ResponseImpl;
import tomastk.shelty.services.impl.AdminSecurityContextHandler;
import tomastk.shelty.user.Role;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class EspecieController {

    public static final String ENTITY_NAME = "especie";
    @Autowired
    private EspecieImpl service;

    @Autowired
    private ResponseImpl responseService;

    @Autowired
    private Logger logger;

    @GetMapping("especies")
    public ResponseEntity<MensajeResponse> getAllEspecies() {
        return responseService.sendSuccessResponse(
                null,
                service.getAll(),
                HttpStatus.OK
        );
    }

    @PostMapping(ENTITY_NAME)
    public ResponseEntity<MensajeResponse> postEspecie(@RequestBody EspecieDTO especie) {
        Map<String, String> errors = new HashMap<>();

        if (AdminSecurityContextHandler.getUserRole() != Role.ADMIN) {
            errors.put(Messages.nonAuthorizatedError, Messages.detailNonAuthorizatedError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.UNAUTHORIZED);
        }

        Especie especieToCreate = Especie.builder()
                .nombre(especie.getNombre())
                .descripcion(especie.getDescripcion())
                .img_url(especie.getImg_url()).build();

        EspecieValidator validator = new EspecieValidator();
        errors = validator.validate(especieToCreate);

        if (!errors.isEmpty()) {
            return responseService.sendErrorResponse(
                    errors,
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            Especie especieCreated = service.save(especieToCreate);
            return responseService.sendSuccessResponse(
                    Messages.detailsSuccessCreation(ENTITY_NAME),
                    especieCreated,
                    HttpStatus.CREATED
            );

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            errors.put(Messages.creationError, Messages.detailCreationError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("especie/{id}")
    public ResponseEntity<MensajeResponse> putEspecie(@PathVariable long id, @RequestBody EspecieDTO especie) {
        Map<String, String> errors = new HashMap<>();

        if (AdminSecurityContextHandler.getUserRole() != Role.ADMIN) {
            errors.put(Messages.nonAuthorizatedError, Messages.detailNonAuthorizatedError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.UNAUTHORIZED);
        }

        Especie especieToUpdate = service.findById(id);

        if (especieToUpdate == null) {
            errors.put(Messages.nonFoundError, Messages.detailNotFoundError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.NOT_FOUND);
        }

        try {
            especieToUpdate.setDescripcion(especie.getDescripcion());
            especieToUpdate.setNombre(especie.getNombre());
            especieToUpdate.setImg_url(especie.getImg_url());
            service.save(especieToUpdate);

            return responseService.sendSuccessResponse(
                    Messages.detailsSuccessEdition(ENTITY_NAME),
                    especieToUpdate,
                    HttpStatus.CREATED
            );

        } catch (DataAccessException ex) {
            logger.error(ex.getMessage());
            errors.put(Messages.editionError, Messages.detailEditionError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("especie/{id}")
    public ResponseEntity<MensajeResponse> deleteEspecie(@PathVariable long id) {
        Map<String, String> errors = new HashMap<>();

        if (AdminSecurityContextHandler.getUserRole() != Role.ADMIN) {
            errors.put(Messages.nonAuthorizatedError, Messages.detailNonAuthorizatedError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.UNAUTHORIZED);
        }

        if (!service.existsById(id)) {
            errors.put(Messages.nonFoundError, Messages.detailNotFoundError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.NOT_FOUND);
        }

        try {
            Especie especieEliminada = service.deleteById(id);

            return responseService.sendSuccessResponse(
                    Messages.detailsSuccessDeleting(ENTITY_NAME),
                    especieEliminada,
                    HttpStatus.CREATED
            );

        } catch (DataAccessException ex) {

            logger.error(ex.getMessage());

            errors.put(Messages.deletingError, Messages.detailDeletingError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
