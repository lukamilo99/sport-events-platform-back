package sport.app.sport_connecting_people.entity.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sport.app.sport_connecting_people.entity.request.Request;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("REQUEST_NOTIFICATION")
public class RequestNotification extends Notification {

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private Request request;
}
