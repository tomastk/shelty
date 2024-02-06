package tomastk.shelty.models.validators;

import tomastk.shelty.models.dtos.RefugioDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefugioValidator extends Validator<RefugioDTO> {


    public RefugioValidator() {
        this.errors = new HashMap<>();
    }

    @Override
    public Map<String, String> validate(RefugioDTO refugioDTO) {
        Object[] nonNullableFields = {refugioDTO.getNombre(), refugioDTO.getImgUrl()};
        if (this.areNullFields(nonNullableFields)){
            this.errors.put("null_fields", "There are some null fields in the request");
            return this.getErrors();
        }
        return this.getErrors();
    }

}
