package dat.dto;

import dat.security.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String username;
    private String password;


    public UserDTO(int id, String username) {
        this.id = id;
        this.username = username;

    }

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
    }
}