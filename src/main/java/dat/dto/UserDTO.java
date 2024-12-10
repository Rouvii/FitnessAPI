package dat.dto;

import dat.security.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Purpose:
 *
 * @author: Kevin Løvstad Schou
 */

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

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}
