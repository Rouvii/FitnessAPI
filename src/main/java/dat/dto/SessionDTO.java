package dat.dto;

import dat.entities.Exercise;
import dat.entities.Session;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor

    public class SessionDTO {
        private int id;
        private String name;
        private UserDTO user; // The user associated with the session
        private List<ExerciseDTO> exercises; // List of exercises associated with the session

        public SessionDTO(Session session) {
            this.id = session.getId();
            this.name = session.getName();
            this.user = new UserDTO(session.getUser()); // Convert User to UserDTO
            this.exercises = session.getExercises().stream()
                    .map(ExerciseDTO::new) // Convert Exercise to ExerciseDTO
                    .collect(Collectors.toList());
        }
    }