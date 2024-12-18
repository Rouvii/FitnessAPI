package dat.dto;

import dat.entities.Exercise;
import dat.entities.MuscleGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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
    private List<SetDTO> sets = new ArrayList<>();

    public ExerciseDTO(Exercise exercise) {
        this.id = exercise.getId();  // Ensure the ID is correctly assigned from the Exercise entity
        this.name = exercise.getName();
        this.muscleGroup = exercise.getMuscleGroup();
        this.description = exercise.getDescription();
        this.sets = exercise.getSets().stream()
                .map(SetDTO::new)
                .collect(Collectors.toList());
    }

    public ExerciseDTO(String name, MuscleGroup muscleGroup, String description, List<SetDTO> sets, int id) {
        this.id = id;  // Ensure the ID is set
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.description = description;
        this.sets = sets;
    }

}