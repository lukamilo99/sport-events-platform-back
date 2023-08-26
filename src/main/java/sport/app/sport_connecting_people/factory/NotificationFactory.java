package sport.app.sport_connecting_people.factory;

import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.entity.enums.NotificationType;
import sport.app.sport_connecting_people.factory.creator.EventInvitationNotificationCreator;
import sport.app.sport_connecting_people.factory.creator.FriendRequestNotificationCreator;
import sport.app.sport_connecting_people.factory.creator.NotificationCreator;

@Component
public class NotificationFactory {

    public NotificationCreator getNotificationCreator(NotificationType type) {
        switch(type) {
            case FRIEND_REQUEST:
                return new FriendRequestNotificationCreator();
            case EVENT_INVITATION:
                return new EventInvitationNotificationCreator();
            default:
                throw new IllegalArgumentException("Invalid notification type");
        }
    }
}
