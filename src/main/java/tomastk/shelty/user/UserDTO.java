package tomastk.shelty.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class UserDTO {
    private String token;
    private String authError;
    private String username;
    private Role role;
}
