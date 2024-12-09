package dat.entities;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Purpose:
 *
 * @author: Kevin Løvstad Schou
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Set {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Nullable
    private int id;
    private int reps;
    private int weight;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

}
