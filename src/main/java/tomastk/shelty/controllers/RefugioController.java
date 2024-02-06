package tomastk.shelty.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tomastk.shelty.config.Messages;
import tomastk.shelty.services.impl.ResponseImpl;
import tomastk.shelty.services.impl.SecurityContextHandler;
import tomastk.shelty.models.dtos.RefugioDTO;
import tomastk.shelty.models.entities.Animal;
import tomastk.shelty.models.entities.Refugio;
import tomastk.shelty.models.payloads.CreateRefugioRequest;
import tomastk.shelty.models.payloads.MensajeResponse;
import tomastk.shelty.models.validators.RefugioValidator;
import tomastk.shelty.services.impl.AnimalImpl;
import tomastk.shelty.services.impl.RefugioImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")


public class RefugioController {

    public static final String ENTITY_NAME = "refugio";
    @Autowired
    RefugioImpl service;
    @Autowired
    AnimalImpl animalService;
    @Autowired
    Logger logger;

    @Autowired
    private ResponseImpl responseService;

    // Get
    @GetMapping("refugios")
    public ResponseEntity<List> getAll() {
        List<Refugio> refugios = service.getAll();
        if (refugios.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(refugios, HttpStatus.OK);
    }

    @GetMapping("refugio/{id}")
    public ResponseEntity<MensajeResponse> get(@PathVariable int id) {
        Map<String, String> errors = new HashMap<>();

        Refugio refugioFinded = service.findById(id);
        if (refugioFinded == null) {
            errors.put(Messages.nonFoundError, Messages.detailNotFoundError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.NOT_FOUND);
        }

        RefugioDTO refugioBuilded = buildRefugioDTO(refugioFinded);

        return responseService.sendSuccessResponse(
                null,
                refugioBuilded,
                HttpStatus.OK
        );
    }

    // Post
    @PostMapping("refugio")
    public ResponseEntity<MensajeResponse> create(@RequestBody CreateRefugioRequest request) {
        Map<String, String> errors;

        RefugioValidator validator = new RefugioValidator();

        long userRequestId = SecurityContextHandler.getUserId();

        List<Long> administradores = List.of(userRequestId);

        Refugio entityToCreate = Refugio.builder()
                .imgUrl(request.getImgUrl())
                .nombre(request.getNombre())
                .animales(new ArrayList<>())
                .administradores(administradores)
                .build();
        errors = validator.validate(buildRefugioDTO(entityToCreate));

        if (!errors.isEmpty()){
            return responseService.sendErrorResponse(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            Refugio entitySaved = service.save(entityToCreate);
            System.out.println(entitySaved);
            return responseService.sendSuccessResponse(
                    Messages.detailsSuccessCreation(ENTITY_NAME),
                    entitySaved,
                    HttpStatus.CREATED);
        } catch (DataAccessException de) {
            logger.error(de.getMessage());
            errors.put(Messages.creationError, Messages.detailCreationError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("refugio/{id_refugio}/add-animal")
    public ResponseEntity<MensajeResponse> addAnimal(@RequestParam int id, @PathVariable int id_refugio) {

        Map<String, String> errors = new HashMap<>();
        Map<String, Object> response = new HashMap<>();

        Animal animalToAssign = animalService.findById(id);
        Refugio refugio = service.findById(id_refugio);

        if (animalToAssign == null || refugio == null ) {
            errors.put(Messages.asignatingError, Messages.detailNullFieldError("animal", "refugio"));
            return responseService.sendErrorResponse(
                    errors,
                    HttpStatus.BAD_REQUEST
            );
        }

        if (animalToAssign.getRefugio() != null ) {
            errors.put(Messages.conflictError, "El animal que deseas asignar ya posee un refugio");
            return responseService.sendErrorResponse(
                    errors,
                    HttpStatus.CONFLICT
            );
        }

        animalToAssign.setRefugio(refugio);

        try {
            animalService.save(animalToAssign);
            response.put("animal_id", animalToAssign.getId());
            response.put("refugio_id", refugio.getId());

            return responseService.sendSuccessResponse(
                    "El refugio ha sido asignado con éxito al animal",
                    response,
                    HttpStatus.CREATED
            );

        } catch (DataAccessException ex ) {
            errors.put(Messages.asignatingError, "Ha ocurrido un error al guardar el animal con su nuevo refugio");
            logger.error(ex.getMessage());
            return responseService.sendErrorResponse(
                    errors,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }


    }

    // Put
    @PutMapping("refugio/{entityRequestID}")
    public ResponseEntity<MensajeResponse> update(@PathVariable int entityRequestID, @RequestBody CreateRefugioRequest entityData){
        // TODO: VALIDAR CRENDENCIALES
        Map<String, String> errors = new HashMap<>();

        Refugio refugioToUpdate = service.findById(entityRequestID);

        if (refugioToUpdate == null) {
            errors.put(Messages.nonFoundError, Messages.detailNotFoundError(ENTITY_NAME));

            return responseService.sendErrorResponse(
                    errors,
                    HttpStatus.NOT_FOUND
            );

        }

        long userRequestID = SecurityContextHandler.getUserId();

        if (!refugioToUpdate.getAdministradores().contains(userRequestID)) {
            errors.put(Messages.nonAuthorizatedError, Messages.detailNonAuthorizatedError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.UNAUTHORIZED);
        }

        refugioToUpdate.setNombre(entityData.getNombre());
        refugioToUpdate.setImgUrl(entityData.getImgUrl());
        RefugioValidator validator = new RefugioValidator();
        errors = validator.validate(buildRefugioDTO(refugioToUpdate));

        if (!errors.isEmpty()){
            return responseService.sendErrorResponse(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            service.save(refugioToUpdate);

            return responseService.sendSuccessResponse(
                    Messages.detailsSuccessCreation(ENTITY_NAME),
                    refugioToUpdate,
                    HttpStatus.CREATED
            );

        } catch (DataAccessException ex) {
            logger.error(ex.getMessage());
            errors.put(Messages.editionError, Messages.detailEditionError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("refugio/{id}")
    public ResponseEntity<MensajeResponse> delete(@PathVariable int id){
        Map<String, String> errors = new HashMap<>();

        Refugio refugioToDelete = service.findById(id);

        if(refugioToDelete == null) {
            errors.put(Messages.nonFoundError, Messages.detailNotFoundError(ENTITY_NAME));
            return responseService.sendErrorResponse(
                    errors,
                    HttpStatus.NOT_FOUND
            );
        }

        long userRequestID = SecurityContextHandler.getUserId();

        if (!refugioToDelete.getAdministradores().contains(userRequestID)) {
            errors.put(Messages.nonAuthorizatedError, Messages.detailNonAuthorizatedError(ENTITY_NAME));
            return responseService.sendErrorResponse(
                    errors,
                    HttpStatus.UNAUTHORIZED
            );
        }

        try {
            service.delete(refugioToDelete);

            return responseService.sendSuccessResponse(
                    Messages.detailsSuccessEdition(ENTITY_NAME),
                    refugioToDelete,
                    HttpStatus.OK
            );

        } catch (DataAccessException ex) {
            logger.error(ex.getMessage());
            errors.put(Messages.editionError, Messages.detailDeletingError(ENTITY_NAME));
            return responseService.sendErrorResponse(
                    errors,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /* BUILD ANIMAL DTO */

    public RefugioDTO buildRefugioDTO(Refugio refugio) {
        return RefugioDTO.builder()
                .id(refugio.getId())
                .nombre(refugio.getNombre())
                .animales(refugio.getAnimales())
                .imgUrl(refugio.getImgUrl())
                .build();
    }

}
