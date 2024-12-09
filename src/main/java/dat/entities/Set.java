package dat.entities;

import dat.dto.SetDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a Set in a workout, containing repetitions and weight for an exercise.
 *
 * @author: Kevin Løvstad Schou
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Set {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // Primary key for Set, auto-generated.

    private int reps;  // Number of repetitions for the set.
    private int weight; // Weight lifted for the set.

    @ManyToOne(fetch = FetchType.LAZY)  // Use LAZY fetching for performance optimization
    @JoinColumn(name = "exercise_id", nullable = false) // Ensures Set must be linked to an Exercise.
    private Exercise exercise; // The Exercise this Set belongs to.

    // Optional: Constructor for creating a Set object easily.
    public Set(int reps, int weight, Exercise exercise) {
        this.reps = reps;
        this.weight = weight;
        this.exercise = exercise;
    }

    // If you want to map SetDTO to Set entity, you can use a method like this:
    public Set toEntity(SetDTO setDTO, Exercise exercise) {
        Set set = new Set();
        set.setReps(setDTO.getReps());
        set.setWeight(setDTO.getWeight());
        set.setExercise(exercise);  // Linking the Exercise entity.
        return set;
    }
}
