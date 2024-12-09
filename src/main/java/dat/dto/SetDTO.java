package dat.dto;

import dat.entities.Exercise;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import dat.entities.Set;

/**
 * Purpose:
 *
 * @author: Kevin Løvstad Schou
 */

@Getter
@Setter
@NoArgsConstructor

public class SetDTO {
    private int id;
    private int reps;
    private int weight;
    private Exercise exercise;

    public SetDTO(int id, int reps, int weight) {
        this.id = id;
        this.reps = reps;
        this.weight = weight;
    }


    public SetDTO(Set set) {
        this.id = set.getId();
        this.reps = set.getReps();
        this.weight = set.getWeight();
        this.exercise = set.getExercise();

    }
}
