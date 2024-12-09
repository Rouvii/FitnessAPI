package dat.dto;

import dat.entities.Exercise;
import dat.entities.MuscleNigger;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kevin
 */


@Setter
@Getter
@NoArgsConstructor
public class ExerciseDTO {
    private int id;
    private String name;
    private MuscleNigger muscleGroup;
    private String description;
    private List<SetDTO> sets = new ArrayList<>();
    private SessionDTO session;



    public ExerciseDTO(int id, String name, MuscleNigger muscleGroup, String description, List<SetDTO> sets, SessionDTO session) {
        this.id = id;
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.description = description;
        this.sets = sets;
        this.session = session;
    }

    public ExerciseDTO(Exercise exercise){
        this.id = exercise.getId();
        this.name = exercise.getName();
        this.muscleGroup = exercise.getMuscleGroup();
        this.description = exercise.getDescription();
        this.sets = exercise.getSets().stream()
                .map(SetDTO::new)
                .collect(java.util.stream.Collectors.toList());
        this.session = new SessionDTO(exercise.getSession());
    }

}
