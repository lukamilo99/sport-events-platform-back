package sport.app.sport_connecting_people.factory.creator;

import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.entity.notification.Notification;

public interface NotificationCreator {

    Notification createNotification(User recipient, Object context);
}
