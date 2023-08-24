package sport.app.sport_connecting_people.entity.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sport.app.sport_connecting_people.entity.Friendship;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("FRIEND_REQUEST")
public class FriendRequestNotification extends Notification {

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "friendship")
    private Friendship friendship;
}
