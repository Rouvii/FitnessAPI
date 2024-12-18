package dat.entities;

import dat.security.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Purpose:
 *
 * @author: Kevin Løvstad Schou
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Workout {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private LocalDate date;
    //Session relation
    @ManyToOne
    private Session session;
    //User relation
    @ManyToOne
    private User user;

//    @OneToMany
//    private List<Set> sets;

    public Workout(LocalDate date, Session session, User user) {
        this.date = date;
        this.session = session;
        this.user = user;
    }

    public Workout(int id, LocalDate date, Session session, User user) {
        this.id = id;
        this.date = date;
        this.session = session;
        this.user = user;
    }

    @PreUpdate
    @PrePersist
    protected void onCreate() {
        date = LocalDate.now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workout workout = (Workout) o;
        return id == workout.id &&
                Objects.equals(date, workout.date) &&
                Objects.equals(session, workout.session) &&
                Objects.equals(user, workout.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, session, user);
    }

}
