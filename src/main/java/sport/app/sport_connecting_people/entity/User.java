package sport.app.sport_connecting_people.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import sport.app.sport_connecting_people.entity.enums.AuthenticationProvider;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    private String password;

    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthenticationProvider provider;

    private String providerId;

    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    private boolean isEnabled;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "eventCreator")
    private List<Event> eventsCreated;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "participants")
    private List<Event> eventsParticipatedIn;

    public User() {
        this.eventsCreated = new ArrayList<>();
        this.eventsParticipatedIn = new ArrayList<>();
        this.isEnabled = true;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
