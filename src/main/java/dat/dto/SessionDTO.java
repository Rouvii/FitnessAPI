package dat.dto;

import dat.entities.Session;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class SessionDTO {

    private int id;
    private UserDTO user;
    private List<ExerciseDTO> exercises = new ArrayList<>();

    public SessionDTO(Session session) {
        this.id = session.getId();
        this.user = new UserDTO(session.getUser());
        this.exercises = session.getExercises() != null ? session.getExercises().stream()
                .map(ExerciseDTO::new)
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    public void addExercise(ExerciseDTO exercise) {
        this.exercises.add(exercise);
    }
}