package dat.dto;

import dat.entities.Session;
import dat.security.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Purpose:
 *
 * @author: Kevin Løvstad Schou
 */



@Setter
@Getter
@NoArgsConstructor
public class SessionDTO {
    private int id;
    private User user;
    private List<ExerciseDTO> exercise;



    public SessionDTO(int id, User user) {
        this.id = id;
        this.user = user;
    }

    public SessionDTO(Session session){
        this.id = session.getId();
        this.user = session.getUser();
        this.exercise = session.getExercise().stream()
                .map(ExerciseDTO::new)
                .collect(Collectors.toList());
    }

}
