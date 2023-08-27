package sport.app.sport_connecting_people.entity.request;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("EVENT_INVITATION_REQUEST")
public class EventInvitationRequest extends Request {

    private Long eventId;
}
