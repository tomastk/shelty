package tomastk.shelty.controllers;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tomastk.shelty.services.impl.AdminSecurityContextHandler;
import tomastk.shelty.config.Messages;
import tomastk.shelty.models.dtos.AnimalResponseDTO;
import tomastk.shelty.models.entities.Especie;
import tomastk.shelty.services.impl.*;
import tomastk.shelty.models.dtos.AnimalRequestDTO;
import tomastk.shelty.models.entities.Animal;
import tomastk.shelty.models.entities.Refugio;
import tomastk.shelty.models.payloads.MensajeResponse;
import tomastk.shelty.models.validators.AnimalValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AnimalController {

    public static final String ENTITY_NAME = "animal";
    @Autowired
    private AnimalImpl service;
    @Autowired
    private Logger logger;

    @Autowired
    private ResponseImpl responseService;

    @Autowired
    private EspecieImpl especieService;

    @Autowired
    private RefugioImpl refugioService;

    @GetMapping(value = "animales", params = {"page", "size"})
    public ResponseEntity<List> getAll(Pageable pageable) {
        Page<Animal> animales = service.findAll(pageable);
        if (animales.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(animales.getContent(), HttpStatus.OK);
    }

    @GetMapping(value = "animales/byEspecie={id}", params = {"page", "size"})
    public ResponseEntity<MensajeResponse> getByEspecieId(@PathVariable long id, Pageable pageable) {
        return responseService.sendSuccessResponse(
                null,
                service.getByEspecie(id, pageable).getContent(),
                HttpStatus.OK
        );
    }

    @GetMapping("animal/{id}")
    public ResponseEntity<AnimalRequestDTO> getAnimal(@PathVariable int id) {
        Animal animalFinded = service.findById(id);
        if (animalFinded != null) {
            return new ResponseEntity<>(buildAnimalWithIdDTO(animalFinded), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    // Post
    @PostMapping(ENTITY_NAME)
    public ResponseEntity<MensajeResponse> createAnimal(@RequestBody AnimalRequestDTO animalToCreate) {
        long requestUserID = AdminSecurityContextHandler.getUserId();

        Especie animalEspecie = especieService.findById(animalToCreate.getEspecie_id());

        Map<String, String> errors = new HashMap<>();

        if (animalEspecie == null) {
            errors.put(Messages.creationError, Messages.detailNullFieldError(ENTITY_NAME, "especie"));
            return responseService.sendErrorResponse(errors, HttpStatus.BAD_REQUEST);
        }

        Animal newAnimal = Animal.builder()
                .especie(animalEspecie)
                .refugio(null)
                .nombre(animalToCreate.getNombre())
                .imgUrl(animalToCreate.getImg_url())
                .ownerId(requestUserID)
                .build();

        AnimalValidator validator = new AnimalValidator();
        errors = validator.validate(buildAnimalDTO(newAnimal));

        if (!errors.isEmpty()){
            return responseService.sendErrorResponse(errors, HttpStatus.BAD_REQUEST);
        }
        try {
            service.save(newAnimal);
        } catch (DataAccessException de) {
            errors.put(Messages.creationError, Messages.detailCreationError(ENTITY_NAME));
            logger.error(de.getMessage());
            return responseService.sendErrorResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseService.sendSuccessResponse(
                Messages.detailsSuccessCreation(ENTITY_NAME),
                buildAnimalWithIdDTO(newAnimal),
                HttpStatus.CREATED
        );
    }

    // Put
    @PutMapping("animal/{id}")
    public ResponseEntity<MensajeResponse> updateAnimal(@PathVariable int id, @RequestBody AnimalRequestDTO animalData){
        Animal animalToUpdate = service.findById(id);
        Especie newAnimalEspecie = especieService.findById(animalData.getEspecie_id());

        if (newAnimalEspecie == null) {
            newAnimalEspecie = animalToUpdate.getEspecie();
        }

        Map<String, String> errors = new HashMap<>();

        if (animalToUpdate == null) {
            errors.put(Messages.nonFoundError, Messages.detailNotFoundError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.NOT_FOUND);
        }

        boolean userIsAuthorizated = this.isUserAuthorized(animalToUpdate);

        if (!userIsAuthorizated) {
            errors.put(Messages.nonAuthorizatedError, Messages.detailNonAuthorizatedError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.UNAUTHORIZED);
        }

        animalToUpdate.setNombre(animalData.getNombre());
        animalToUpdate.setImgUrl(animalData.getImg_url());
        animalToUpdate.setEspecie(newAnimalEspecie);

        AnimalValidator validator = new AnimalValidator();
        errors = validator.validate(buildAnimalDTO(animalToUpdate));

        if (!errors.isEmpty()){
            return responseService.sendErrorResponse(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            Animal animalUpdated = service.save(animalToUpdate);
            return responseService.sendSuccessResponse(
                    Messages.detailsSuccessEdition(ENTITY_NAME),
                    buildAnimalWithIdDTO(animalUpdated),
                    HttpStatus.OK
            );
        } catch (DataAccessException ex) {
            logger.error(ex.getMessage());
            errors.put(Messages.editionError, Messages.detailEditionError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Delete
    @DeleteMapping("animal/{id}")
    public ResponseEntity<MensajeResponse> deleteAnimal(@PathVariable int id){
        Animal animalToDelete = service.findById(id);
        Map<String, String> errors = new HashMap<>();
        if(animalToDelete == null) {
            errors.put(Messages.nonFoundError, Messages.detailNotFoundError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.NOT_FOUND);
        }


        boolean userIsAuthorizated = this.isUserAuthorized(animalToDelete);

        if (!userIsAuthorizated){
            errors.put(Messages.nonAuthorizatedError, Messages.detailNonAuthorizatedError(ENTITY_NAME));
            return responseService.sendErrorResponse(errors, HttpStatus.UNAUTHORIZED);
        }


        try {
            service.delete(animalToDelete);
            return responseService.sendSuccessResponse(
                    Messages.detailsSuccessDeleting(ENTITY_NAME),
                    buildAnimalWithIdDTO(animalToDelete),
                    HttpStatus.OK
            );

        } catch (DataAccessException ex) {
            logger.error(ex.getMessage());
            errors.put(Messages.deletingError, Messages.detailDeletingError(ENTITY_NAME));
            return responseService.sendErrorResponse(
                    errors,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Checks if the user is authorized to access the animal information.
     * @param animalToCheck The animal to check authorization for.
     * @return True if the user is authorized, false otherwise.
     */
    public boolean isUserAuthorized(Animal animalToCheck) {
        return animalToCheck.getOwnerId() == AdminSecurityContextHandler.getUserId() || AdminSecurityContextHandler.isAdmin();
    }


    /* BUILD ANIMAL */
    public Animal buildAnimal(AnimalRequestDTO animalRequestDTO) {
        return buildAnimal(animalRequestDTO, null, null);
    }

    public Animal buildAnimal(AnimalRequestDTO animalRequestDTO, Especie animalEspecie, Refugio animalRefugio) {
        return Animal.builder()
                .especie(animalEspecie)
                .nombre(animalRequestDTO.getNombre())
                .refugio(animalRefugio)
                .imgUrl(animalRequestDTO.getImg_url())
                .build();
    }

    public AnimalResponseDTO buildAnimalWithIdDTO(Animal animal) {
        AnimalResponseDTO animalWithIdDTO = new AnimalResponseDTO();
        animalWithIdDTO.setNombre(animal.getNombre());

        if (animal.getRefugio() != null) {
            animalWithIdDTO.setRefugio_id(animal.getRefugio().getId());
        }

        animalWithIdDTO.setImg_url(animal.getImgUrl());
        animalWithIdDTO.setEspecie_id(animal.getEspecie().getId());
        animalWithIdDTO.setImg_url(animal.getImgUrl());
        animalWithIdDTO.setId(animal.getId());
        return animalWithIdDTO;
    }

    /* BUILD ANIMAL DTO
    *  */
    public AnimalRequestDTO buildAnimalDTO(Animal animal) {
        AnimalRequestDTO animalRequestDTO = AnimalRequestDTO.builder()
                .especie_id(animal.getEspecie().getId())
                .nombre(animal.getNombre())
                .refugio_id(0)
                .img_url(animal.getImgUrl())
                .build();
        if (animal.getRefugio() != null) {
            animalRequestDTO.setRefugio_id(animal.getRefugio().getId());
        }
        return animalRequestDTO;
    }


}
