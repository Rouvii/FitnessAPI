package dat.dto;

import dat.entities.Exercise;
import dat.entities.MuscleGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ExerciseDTO {

    private int id;
    private String name;
    private MuscleGroup muscleGroup;
    private String description;
    private List<SetDTO> sets;
    private int sessionId; // Use sessionId instead of full SessionDTO to avoid circular reference

    public ExerciseDTO(Exercise exercise) {
        this.id = exercise.getId();
        this.name = exercise.getName();
        this.muscleGroup = exercise.getMuscleGroup();
        this.description = exercise.getDescription();
        this.sets = exercise.getSets().stream().map(SetDTO::new).collect(Collectors.toList());
        this.sessionId = exercise.getSession().getId(); // Only store sessionId
    }

    public ExerciseDTO(String name, MuscleGroup muscleGroup, String description, List<SetDTO> sets, int sessionId) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.description = description;
        this.sets = sets;
        this.sessionId = sessionId;
    }
}