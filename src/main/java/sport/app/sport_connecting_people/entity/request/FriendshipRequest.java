package sport.app.sport_connecting_people.entity.request;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("FRIENDSHIP_REQUEST")
public class FriendshipRequest extends Request {


}
