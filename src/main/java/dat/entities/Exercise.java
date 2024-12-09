package dat.entities;

import dat.dto.ExerciseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Purpose:
 *
 * @author: Kevin Løvstad Schou
 */





@Entity
@NoArgsConstructor
@Setter
@Getter
public class Exercise {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private String name;
    @Enumerated(jakarta.persistence.EnumType.STRING)
    private MuscleGroup muscleGroup;
    private String description;
    @OneToMany
    private List<Set> sets;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;



    public Exercise(String name, MuscleGroup muscleGroup, String description, List<Set> sets, Session session) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.description = description;
        this.sets = sets;
        this.session = session;
    }

    public Exercise(ExerciseDTO exerciseDTO) {
        this.name = exerciseDTO.getName();
        this.muscleGroup = exerciseDTO.getMuscleGroup();
        this.description = exerciseDTO.getDescription();
       // this.sets = exerciseDTO.getSets();
       // this.session = exerciseDTO.getSession();
    }


}
