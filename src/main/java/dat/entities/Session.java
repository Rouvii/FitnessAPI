package dat.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dat.dto.SessionDTO;
import dat.security.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String name;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "sessions")
    @JsonManagedReference
    private List<Exercise> exercise;

    public Session(SessionDTO sessionDTO, List<Exercise> exercises, User user) {
        this.name = sessionDTO.getName();
        this.user = user;
        this.exercise = exercises; // Properly mapped list of exercises fetched beforehand
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return id == session.id &&
                Objects.equals(user, session.user) &&
                Objects.equals(exercise, session.exercise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, exercise);
    }
}