package dat.entities;

import dat.dao.IDao;
import dat.dto.SessionDTO;
import dat.entities.Exercise;
import dat.security.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private User user; // the User object will be fetched from the database

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "session_exercise",
            joinColumns = @JoinColumn(name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    private List<Exercise> exercises; // exercises linked to the session

    // Constructor from DTO
    public Session(SessionDTO sessionDTO, User user, IDao<Exercise> exerciseDao) {
        this.id = sessionDTO.getId();
        this.user = user; // Set the user directly, assuming it was fetched in the controller
        this.name = sessionDTO.getName();

        // Fetch exercises from the database by ID
        this.exercises = sessionDTO.getExercises().stream()
                .map(exerciseDTO -> exerciseDao.getById(exerciseDTO.getId())) // Fetch Exercise from DB
                .filter(exercise -> exercise != null) // Make sure the exercise is valid
                .collect(Collectors.toList());
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
