package tomastk.shelty.models.payloads;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@ToString
@Builder
public class MensajeResponse implements Serializable {

    private String message;
    private Object objeto;

}
