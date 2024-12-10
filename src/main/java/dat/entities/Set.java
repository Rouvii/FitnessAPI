package dat.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dat.dto.SetDTO;
import dat.entities.Exercise;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Objects;

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
    @JsonBackReference
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Set set = (Set) o;
        return id == set.id &&
                reps == set.reps &&
                weight == set.weight &&
                Objects.equals(exercise, set.exercise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reps, weight, exercise);
    }
}