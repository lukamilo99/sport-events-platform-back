package sport.app.sport_connecting_people.factory.creator;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.entity.Friendship;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.entity.notification.Notification;
import sport.app.sport_connecting_people.mapper.NotificationMapper;

@NoArgsConstructor
@AllArgsConstructor
@Component
public class FriendRequestNotificationCreator implements NotificationCreator {

    private NotificationMapper notificationMapper;

    @Override
    public Notification createNotification(User recipient, Object context) {
        Friendship friendship = (Friendship) context;
        return notificationMapper.mapToFriendRequestNotification(recipient, friendship);
    }
}
