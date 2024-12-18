package dat.dto;

import dat.entities.Exercise;
import dat.entities.Session;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

import dat.dto.UserDTO;

@Getter
@Setter
@NoArgsConstructor
public class SessionDTO {

    private int id;
    private UserDTO user;
    private String name;
    private List<Integer> exerciseIds;

    public SessionDTO(Session session) {
        this.id = session.getId();
        this.name = session.getName();
        this.user = new UserDTO(session.getUser());
        this.exerciseIds = session.getExercise().stream()
                .map(Exercise::getId)
                .collect(Collectors.toList());
    }
}