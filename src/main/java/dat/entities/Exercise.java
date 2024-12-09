package dat.entities;

import dat.dto.ExerciseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents an Exercise in a workout session.
 *
 * @author: Kevin Løvstad Schou
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // Primary key for Exercise.

    private String name;  // Name of the exercise.

    @Enumerated(EnumType.STRING)
    private MuscleGroup muscleGroup;  // The muscle group targeted by the exercise.

    private String description;  // Description of the exercise.

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Set> sets;  // List of sets for this exercise.

    @ManyToOne(fetch = FetchType.LAZY)  // The session this exercise belongs to.
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;  // The session for the exercise.

    // Constructor to initialize Exercise entity with all fields.
    public Exercise(String name, MuscleGroup muscleGroup, String description, List<Set> sets, Session session) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.description = description;
        this.sets = sets;
        this.session = session;
    }

    // Constructor to convert from ExerciseDTO to Exercise entity.
    public Exercise(ExerciseDTO exerciseDTO) {
        this.name = exerciseDTO.getName();
        this.muscleGroup = exerciseDTO.getMuscleGroup();
        this.description = exerciseDTO.getDescription();

        // Convert SetDTO to Set entity.
        this.sets = exerciseDTO.getSets().stream()
                .map(setDTO -> new Set(setDTO.getReps(), setDTO.getWeight(), this)) // Assuming each SetDTO has reps and weight.
                .toList();

        // Convert SessionDTO to Session entity.
        if (exerciseDTO.getSession() != null) {
            this.session = new Session(exerciseDTO.getSession());
        }
    }
}
