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

    private String username;

    public UserDTO(int id, String username) {
        this.username = username;
    }

    public UserDTO(User user) {
        this.username = user.getUsername();
    }
}
