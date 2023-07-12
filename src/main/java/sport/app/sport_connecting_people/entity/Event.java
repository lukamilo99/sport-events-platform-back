package sport.app.sport_connecting_people.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private Integer capacity;

    @NotBlank
    private String sport;

    @NotBlank
    private String location;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Date date;

    @ManyToOne
    @JoinColumn(name = "eventCreator", referencedColumnName = "id")
    private User eventCreator;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_user",
    joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "participant_id", referencedColumnName = "id"))
    private Set<User> participants;

    public Event(String name, Integer capacity, String sport, String location, Date date) {
        this.name = name;
        this.capacity = capacity;
        this.sport = sport;
        this.location = location;
        this.date = date;
    }

    public void setEventCreator(User eventCreator) {
        this.eventCreator = eventCreator;
        this.participants.add(eventCreator);
    }

    public void addParticipant(User user) {
        this.participants.add(user);
    }

    public void removeParticipant(User user) {
        this.participants.remove(user);
    }
}
