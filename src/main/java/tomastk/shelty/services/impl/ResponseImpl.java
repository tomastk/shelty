package tomastk.shelty.services.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tomastk.shelty.services.IResponseService;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseImpl implements IResponseService {

    @Override
    public ResponseEntity sendErrorResponse(Map errors, HttpStatus code) {
        return new ResponseEntity(errors, code);
    }

    @Override
    public ResponseEntity sendSuccessResponse(String message, Object o, HttpStatus code) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("response", o);
        return new ResponseEntity(response, code);
    }
}
