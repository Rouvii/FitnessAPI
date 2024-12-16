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
    private List<Integer> sessionIds = new ArrayList<>(); // Use sessionIds to handle many-to-many relationship

    public ExerciseDTO(Exercise exercise) {
        this.id = exercise.getId();
        this.name = exercise.getName();
        this.muscleGroup = exercise.getMuscleGroup();
        this.description = exercise.getDescription();
        this.sets = exercise.getSets() != null ? exercise.getSets().stream().map(SetDTO::new).collect(Collectors.toList()) : new ArrayList<>();
        this.sessionIds = exercise.getSessions() != null ? exercise.getSessions().stream().map(session -> session.getId()).collect(Collectors.toList()) : new ArrayList<>();
    }

    public ExerciseDTO(String name, MuscleGroup muscleGroup, String description, List<SetDTO> sets, List<Integer> sessionIds) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.description = description;
        this.sets = sets;
        this.sessionIds = sessionIds;
    }
}