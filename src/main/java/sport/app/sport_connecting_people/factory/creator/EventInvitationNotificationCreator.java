package sport.app.sport_connecting_people.factory.creator;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.entity.Event;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.entity.notification.Notification;
import sport.app.sport_connecting_people.mapper.NotificationMapper;

@NoArgsConstructor
@AllArgsConstructor
@Component
public class EventInvitationNotificationCreator implements NotificationCreator {

    private NotificationMapper notificationMapper;

    @Override
    public Notification createNotification(User recipient, Object context) {
        Event event = (Event) context;
        return notificationMapper.mapToEventInvitationNotification(recipient, event);
    }
}
