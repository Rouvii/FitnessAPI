package dat.entities;

import dat.dto.SessionDTO;
import dat.security.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kevin
 */




@Entity
@NoArgsConstructor
@Getter
@Setter
public class Session {
@Id
@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
private int id;
@ManyToOne
@JoinColumn(name = "user_id", nullable = false)
private User user;
@Getter
@OneToMany
private List<Exercise> exercise;

    public Session(int id, User user, List<Exercise> exercise) {
        this.id = id;
        this.user = user;
        this.exercise = exercise;
    }

    public Session(SessionDTO sessionDTO){
        this.id = sessionDTO.getId();
        this.user = sessionDTO.getUser();
        this.exercise = sessionDTO.getExercise().stream()
                .map(Exercise::new)
                .collect(Collectors.toList());
    }


//Bed copilot konfigurer til at virke med den nye entity klasse
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return id == plant.id &&
                maxHeight == plant.maxHeight &&
                price == plant.price &&
                Objects.equals(name, plant.name) &&
                plantType == plant.plantType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, plantType, maxHeight, price);
    }
    */

}
