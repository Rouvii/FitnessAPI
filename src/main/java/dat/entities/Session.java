package dat.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dat.dto.ExerciseDTO;
import dat.dto.SessionDTO;
import dat.security.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String name;

    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "sessions")
    private List<Exercise> exercises = new ArrayList<>();

    public Session(SessionDTO sessionDTO) {
        this.id = sessionDTO.getId();
        this.user = new User(sessionDTO.getUser().getUsername(), sessionDTO.getUser().getPassword());
        this.exercises = sessionDTO.getExercises().stream()
                .map(exerciseDTO -> new Exercise(exerciseDTO, List.of(this)))
                .collect(Collectors.toList());
    }

    public void addExercise(Exercise exercise) {
        // Ryd listen af øvelser i sessionen, så vi kan tilføje den nye øvelse
        this.exercises.clear(); // Fjerner alle eksisterende øvelser i listen

        // Tilføj den nye øvelse
        this.exercises.add(exercise);

        // Sørg for, at sessionen er tilføjet til øvelsens sessionsliste (hvis ikke allerede tilføjet)
        if (!exercise.getSessions().contains(this)) {
            exercise.getSessions().add(this);
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return id == session.id &&
                Objects.equals(user, session.user) &&
                Objects.equals(exercises, session.exercises);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, user, exercises);
    }


}