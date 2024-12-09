package dat.dto;

import dat.entities.Exercise;
import dat.entities.MuscleGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kevin
 */


@Setter
@Getter
@NoArgsConstructor
public class ExerciseDTO {
    private int id;
    private String name;
    private MuscleGroup muscleGroup;
    private String description;
    private List<SetDTO> sets = new ArrayList<>();
    private SessionDTO session;

    /**
     * Fully parameterized constructor.
     */
    public ExerciseDTO(int id, String name, MuscleGroup muscleGroup, String description, List<SetDTO> sets, SessionDTO session) {
        this.id = id;
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.description = description;
        this.sets = (sets != null) ? sets : new ArrayList<>(); // Handle null lists
        this.session = session;
    }

    /**
     * Constructs an ExerciseDTO from an Exercise entity.
     */
    public ExerciseDTO(Exercise exercise) {
        this.id = exercise.getId();
        this.name = exercise.getName();
        this.muscleGroup = exercise.getMuscleGroup();
        this.description = exercise.getDescription();
        this.sets = (exercise.getSets() != null)
                ? exercise.getSets().stream()
                .map(SetDTO::new)
                .collect(Collectors.toList())
                : new ArrayList<>(); // Handle null or empty sets
        this.session = (exercise.getSession() != null) ? new SessionDTO(exercise.getSession()) : null;
    }
}