//package dat.entities;
//
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import dat.dto.SessionDTO;
//import dat.security.entities.User;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//@Entity
//@NoArgsConstructor
//@Getter
//@Setter
//public class Session {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonManagedReference
//    private List<Exercise> exercise;
//
//    public Session(SessionDTO sessionDTO) {
//        this.id = sessionDTO.getId();
//        this.user = new User(sessionDTO.getUser().getUsername(), sessionDTO.getUser().getPassword());
//        this.exercise = sessionDTO.getExercises().stream()
//                .map(exerciseDTO -> new Exercise(exerciseDTO, this))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Session session = (Session) o;
//        return id == session.id &&
//                Objects.equals(user, session.user) &&
//                Objects.equals(exercise, session.exercise);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, user, exercise);
//    }
//}

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

    @ManyToMany
    @JoinTable(
            name = "session_exercise",
            joinColumns = @JoinColumn(name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    @JsonManagedReference
    private List<Exercise> exercises;

    public Session(SessionDTO sessionDTO) {
        this.id = sessionDTO.getId();
        this.user = new User(sessionDTO.getUser().getUsername(), sessionDTO.getUser().getPassword());
        this.exercises = sessionDTO.getExercises().stream()
                .map(exerciseDTO -> new Exercise(exerciseDTO, this))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return id == session.id &&
                Objects.equals(user, session.user) &&
                Objects.equals(exercises, session.exercises);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, exercises);
    }
}