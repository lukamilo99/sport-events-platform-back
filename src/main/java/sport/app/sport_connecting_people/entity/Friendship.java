package sport.app.sport_connecting_people.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sport.app.sport_connecting_people.entity.enums.FriendshipStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "friendship")
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester", referencedColumnName = "id")
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responder", referencedColumnName = "id")
    private User responder;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;
}
