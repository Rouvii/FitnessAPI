package dat.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dat.dto.ExerciseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Enumerated(EnumType.STRING)
    private MuscleGroup muscleGroup;

    private String description;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    @JsonManagedReference
    private List<Set> sets;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    @JsonBackReference
    private List<Session>sessions;


    public Exercise(Integer id, List<Session> session) {
        this.id = id;
        this.sessions = session;
    }

    public Exercise(String name, MuscleGroup muscleGroup, String description, List<Set> sets, List<Session> session) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.description = description;
        this.sets = sets != null ? sets : new ArrayList<>();
        this.sessions = session;
    }

    public Exercise(ExerciseDTO exerciseDTO,List<Session> session) {
        this.name = exerciseDTO.getName();
        this.muscleGroup = exerciseDTO.getMuscleGroup();
        this.description = exerciseDTO.getDescription();
        this.sessions = session;
        this.sets = exerciseDTO.getSets() != null ? exerciseDTO.getSets().stream()
                .map(setDTO -> new Set(setDTO.getReps(), setDTO.getWeight(), this))
                .toList() : new ArrayList<>();
    }

    public Exercise(Integer integer) {
        this.id = integer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return id == exercise.id &&
                Objects.equals(name, exercise.name) &&
                muscleGroup == exercise.muscleGroup &&
                Objects.equals(description, exercise.description) &&
                Objects.equals(sets, exercise.sets) &&
                Objects.equals(sessions, exercise.sessions);
    }




    @Override
    public int hashCode() {
        return Objects.hash(id, name, muscleGroup, description, sets, sessions);
    }
}