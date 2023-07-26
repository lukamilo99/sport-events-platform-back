package sport.app.sport_connecting_people.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
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

    @NotNull
    private Integer capacity;

    @NotBlank
    private String sport;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private LocalDateTime creationDate;

    @NotBlank
    private String streetName;

    @NotBlank
    private String city;

    @NotNull
    private Double coordinatesLat;

    @NotNull
    private Double coordinatesLon;

    @ManyToOne
    @JoinColumn(name = "eventCreator", referencedColumnName = "id")
    private User eventCreator;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_user",
    joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "participant_id", referencedColumnName = "id"))
    private Set<User> participants;

    public void setEventCreator(User eventCreator) {
        this.eventCreator = eventCreator;
        this.participants = new HashSet<>();
        this.participants.add(eventCreator);
    }

    public void addParticipant(User user) {
        this.participants.add(user);
    }

    public void removeParticipant(User user) {
        this.participants.remove(user);
    }
}
