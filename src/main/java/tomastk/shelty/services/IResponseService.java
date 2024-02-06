package tomastk.shelty.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IResponseService<T>{
    ResponseEntity<T> sendErrorResponse(Map<String, String> errors, HttpStatus code);
    ResponseEntity<T> sendSuccessResponse(String message, Object o, HttpStatus code);
}
