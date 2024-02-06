package tomastk.shelty.models.validators;

import tomastk.shelty.models.entities.Especie;

import java.util.HashMap;
import java.util.Map;

public class EspecieValidator extends Validator<Especie> {

    public EspecieValidator(){
        this.errors = new HashMap<String, String>();
    }

    @Override
    public Map<String, String> validate(Especie objectToValidate) {
        Object[] nonNullableFields = {objectToValidate.getNombre(), objectToValidate.getDescripcion(), objectToValidate.getImg_url()};
        if (this.areNullFields(nonNullableFields)) {
            this.errors.put("campos_nulos", "Hay algunos campos nulos");
            return this.getErrors();
        }
        return this.errors;
    }
}
