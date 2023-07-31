package sport.app.sport_connecting_people.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "eventCreator")
    private List<Event> eventsCreated;

    @ManyToMany(mappedBy = "participants")
    private List<Event> eventsParticipatedIn;

    public User(String email, String firstname, String lastname, String password, String provider) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.provider = AuthenticationProvider.valueOf(provider);
        this.eventsCreated = new ArrayList<>();
        this.eventsParticipatedIn = new ArrayList<>();
    }
}
