package tomastk.shelty.models.validators;

import org.springframework.stereotype.Component;
import tomastk.shelty.auth.RegisterRequest.UserRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRegisterValidator extends Validator<UserRequest> {


    private final int MINIMUN_USERNAME_LENGTH = 4;

    public UserRegisterValidator() {
        this.errors = new HashMap<>();
    }


    @Override
    public Map<String, String> validate(UserRequest objectToValidate) {
        Object[] nonNullableFields = {objectToValidate.getUsername(), objectToValidate.getPassword()};
        if (areNullFields(nonNullableFields)) {
            this.errors.put("null_fields", "There are some null fields");
            return this.getErrors();
        }
        validateUsername(objectToValidate.getUsername());
        validatePassword(objectToValidate.getPassword());
        return this.getErrors();
    }
    private void validateUsername(String username) {
        if (username.length() < MINIMUN_USERNAME_LENGTH) {
            this.errors.put("username", "The username has less than the minimun caractheres");
        }
    }
    private void validatePassword(String password) {
        if (password.length() < 8) {
            this.errors.put("password", "The password is not safe enough");
        }
    }
}
