package tomastk.shelty.models.validators;

import java.util.List;
import java.util.Map;


/* TODO: IMPLEMENTAR MAP PARA LA LISTA DE ERRORES, */
public abstract class Validator<T> {
    protected Map<String, String> errors;

    public abstract Map<String, String> validate(T objectToValidate);
    protected Map<String, String> getErrors() {
        return this.errors;
    };


    public boolean areNullFields(Object[] nonNullableFields) {
        try {
            List.of(nonNullableFields);
        } catch (NullPointerException exception) {
            return true;
        }
        return false;
    }

}
