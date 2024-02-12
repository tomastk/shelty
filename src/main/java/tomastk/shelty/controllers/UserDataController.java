package tomastk.shelty.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tomastk.shelty.models.dtos.UserDataDTO;
import tomastk.shelty.models.entities.UserData;
import tomastk.shelty.models.payloads.MensajeResponse;
import tomastk.shelty.services.impl.AdminSecurityContextHandler;
import tomastk.shelty.services.impl.UserDataImpl;
import tomastk.shelty.user.Role;

@RestController
@RequestMapping("user/")

public class UserDataController {
    @Autowired
    private UserDataImpl service;

    @Autowired
    Logger logger;

    @GetMapping("/{id}")
    public ResponseEntity<UserData> getAnimal(@PathVariable long id) {
        boolean userIsAuthorizated = userIsAuthorizated(id);

        if(!userIsAuthorizated) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserData userFinded = service.findById(id);
        if (userFinded != null) {
            return new ResponseEntity<>(userFinded, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserData> updateUser(@PathVariable long id, @RequestBody UserDataDTO data) {
        UserData userToUpdate = service.findById(id);

        boolean userIsAuthorizated = userIsAuthorizated(id);

        if(!userIsAuthorizated) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (userToUpdate == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        userToUpdate.setDescription(data.getDescription());
        try {
            service.save(userToUpdate);
        } catch (DataAccessException ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(userToUpdate, HttpStatus.CREATED);
    }


    private UserDataDTO buildUserDataDTO(UserData user) {
        return UserDataDTO.builder()
                .description(user.getDescription())
                .build();
    }

    private ResponseEntity<MensajeResponse> sendResponse(String message, Object object, HttpStatus code) {
        MensajeResponse response = MensajeResponse.builder()
                .objeto(object)
                .message(message)
                .build();
        return new ResponseEntity<>(response, code);
    }

    private boolean userIsAuthorizated(long userId) {
        return AdminSecurityContextHandler.getUserId() == userId || AdminSecurityContextHandler.getUserRole() == Role.ADMIN;
    }

}
