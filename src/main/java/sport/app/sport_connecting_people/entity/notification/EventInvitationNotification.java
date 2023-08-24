package sport.app.sport_connecting_people.entity.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sport.app.sport_connecting_people.entity.Event;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("EVENT_REQUEST")
public class EventInvitationNotification extends Notification {

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "event")
    private Event event;
}
