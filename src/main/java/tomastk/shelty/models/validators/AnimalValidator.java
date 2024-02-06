package tomastk.shelty.models.validators;

import lombok.Getter;
import tomastk.shelty.models.dtos.AnimalRequestDTO;

import java.util.HashMap;
import java.util.Map;

@Getter

public class AnimalValidator extends Validator<AnimalRequestDTO>{
    public AnimalValidator(){
       this.errors = new HashMap<String, String>();
    }

    @Override
    public Map<String, String> validate(AnimalRequestDTO animal) {
        Object[] nonNullableFields = {animal.getNombre(), animal.getEspecie_id(), animal.getImg_url()};
        if (this.areNullFields(nonNullableFields)){
            this.errors.put("null_fields", "There are some null fields");
            return this.getErrors();
        }
        return super.getErrors();
    }

}
