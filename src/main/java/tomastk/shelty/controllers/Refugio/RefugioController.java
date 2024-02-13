package tomastk.shelty.controllers.Refugio;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tomastk.shelty.config.Messages;
import tomastk.shelty.models.entities.Picture;
import tomastk.shelty.services.impl.*;
import tomastk.shelty.models.dtos.RefugioDTO;
import tomastk.shelty.models.entities.Animal;
import tomastk.shelty.models.entities.Refugio;
import tomastk.shelty.models.payloads.Refugio.CreateRefugioRequest;
import tomastk.shelty.models.payloads.MensajeResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class RefugioController {

    public static final String ENTITY_NAME = "refugio";
    @Autowired
    RefugioImpl service;
    @Autowired
    AnimalImpl animalService;
    @Autowired
    PictureImpl pictureService;
    @Autowired
    Logger logger;
    @Autowired
    UserDataImpl userService;

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


        return responseService.sendSuccessResponse(
                null,
                refugioFinded,
                HttpStatus.OK
        );
    }

    @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // pa que acepte credenciales
    @GetMapping("refugio/mis-refugios")
    public ResponseEntity<MensajeResponse> getByUserId() {
        long userId = AdminSecurityContextHandler.getUserId();
        List<Refugio> refugios = service.getByUser(userId);
        if (refugios.isEmpty()) {
            return responseService.sendErrorResponse(Map.of(Messages.nonFoundError, Messages.detailNotFoundError(ENTITY_NAME)), HttpStatus.NOT_FOUND);
        }
        System.out.println("Hemos entrado al controlador");
        return responseService.sendSuccessResponse(null, refugios, HttpStatus.OK);
    }

    // Post
    @PostMapping("refugio")
    public ResponseEntity<MensajeResponse> create(@RequestBody CreateRefugioRequest request) {
        Map<String, String> errors = new HashMap<>();
        long userRequestId = AdminSecurityContextHandler.getUserId();

        List<Long> administradores = List.of(userRequestId);
        Picture refugioPicture = pictureService.save(Picture.builder().profilePicture(request.getProfilePicture()).build());

        Refugio entityToCreate = Refugio.builder()
                .administradores(administradores)
                .picture(refugioPicture)
                .creatorID(userRequestId)
                .short_description(request.getShortDescription())
                .build();

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

    @PostMapping("refugio/{refugioId}/add-administrator")
    public ResponseEntity<MensajeResponse> addAdmin(@PathVariable int refugioId, @RequestParam long id) {

        Refugio refugioToEdit = service.findById(refugioId);

        Map<String, String> errors = new HashMap<>();

        boolean administradorNuevoExiste = userService.existsById(id);

        if (!administradorNuevoExiste) {
            errors.put(Messages.nonFoundError, Messages.detailNotFoundError("usuario"));
            return responseService.sendErrorResponse(
                errors,
                HttpStatus.NOT_FOUND
            );
        }

        List<Long> administradores = refugioToEdit.getAdministradores();

        if (administradores.contains(id)) {
            errors.put(Messages.conflictError, "Este usuario ya es un administrador del refugio");
            return responseService.sendErrorResponse(
                    errors,
                    HttpStatus.CONFLICT
            );
        }

        if (!administradores.contains(AdminSecurityContextHandler.getUserId())) {
            errors.put(Messages.nonAuthorizatedError, Messages.detailNonAuthorizatedError(ENTITY_NAME));
            return responseService.sendErrorResponse(
                    errors,
                    HttpStatus.UNAUTHORIZED
            );
        }
        try {
            String response = "Se ha añadido el usuario de id " + id + " como administrador del refugio";
            administradores.add(id);
            refugioToEdit.setAdministradores(administradores);
            service.save(refugioToEdit);
            return responseService.sendSuccessResponse(
                    response,
                    null,
                    HttpStatus.CREATED
            );
        } catch (DataAccessException ex) {
            errors.put(Messages.editionError, Messages.detailEditionError(ENTITY_NAME));
            logger.error(ex.getMessage());
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

    @DeleteMapping("refugio/{id_refugio}/delete-administrator")
    public ResponseEntity<MensajeResponse> deleteAdmin(@PathVariable int id_refugio, @RequestParam long id) {
        Refugio refugioToEdit = service.findById(id_refugio);
        Map<String, String> errors = new HashMap<>();
        if (refugioToEdit == null) {
            errors.put(Messages.nonFoundError, Messages.detailNotFoundError(ENTITY_NAME));
            return responseService.sendErrorResponse(
                    errors,
                    HttpStatus.NOT_FOUND
            );
        }

        if (refugioToEdit.getCreatorID() != AdminSecurityContextHandler.getUserId()) {
            errors.put(Messages.nonAuthorizatedError, "Solo el creador puede eliminar al administrador de un refugio");
            return responseService.sendErrorResponse(
                    errors,
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<Long> administradoresActuales = refugioToEdit.getAdministradores();

        if (!administradoresActuales.contains(id)) {
            errors.put(Messages.deletingError, "El usuario que deseas eliminar no es administrador del refugio");
            return responseService.sendErrorResponse(
                    errors,
                    HttpStatus.BAD_REQUEST
            );
        }

        administradoresActuales.remove(id);
        refugioToEdit.setAdministradores(administradoresActuales);

        try {
            service.save(refugioToEdit);
            return responseService.sendSuccessResponse(
                    "Se ha eliminado correctamente al usuario con id " + id + " como administrador del refugio",
                    refugioToEdit,
                    HttpStatus.CREATED
            );
        } catch (DataAccessException ex) {
            logger.error(ex.getMessage());
            errors.put(Messages.deletingError, "usuario");
            return responseService.sendErrorResponse(
                    errors,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
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

        long userRequestID = AdminSecurityContextHandler.getUserId();

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
                .build();
    }

    /* CREATE REFUGIO AND PICTURE */



}
