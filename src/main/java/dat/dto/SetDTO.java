package dat.dto;

import dat.entities.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SetDTO {
    private int id;
    private int reps;
    private int weight;
    private int exerciseId;

    public SetDTO(int id, int reps, int weight, int exerciseId) {
        this.id = id;
        this.reps = reps;
        this.weight = weight;
        this.exerciseId = exerciseId;
    }

    public SetDTO(Set set) {
        this.id = set.getId();
        this.reps = set.getReps();
        this.weight = set.getWeight();
        this.exerciseId = set.getExercise().getId();
    }
}