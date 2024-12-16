package dat.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.security.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private int id;
    private String username;
    private String password;


    public UserDTO(int id, String username) {
        this.id = id;
        this.username = username;

    }

    public UserDTO(int id,String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
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