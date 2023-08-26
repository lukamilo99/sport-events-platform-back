package sport.app.sport_connecting_people.service.implementation.facade;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.entity.Event;
import sport.app.sport_connecting_people.entity.Friendship;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.entity.enums.NotificationType;
import sport.app.sport_connecting_people.entity.notification.Notification;
import sport.app.sport_connecting_people.factory.NotificationFactory;
import sport.app.sport_connecting_people.factory.creator.NotificationCreator;
import sport.app.sport_connecting_people.service.specification.EventService;
import sport.app.sport_connecting_people.service.specification.FriendshipService;
import sport.app.sport_connecting_people.service.specification.NotificationService;
import sport.app.sport_connecting_people.service.specification.UserService;

@AllArgsConstructor
@Service
public class UserInteractionFacade {

    private FriendshipService friendshipService;
    private EventService eventService;
    private UserService userService;
    private NotificationService notificationService;
    private NotificationFactory notificationFactory;

    @Transactional
    public void sendFriendRequest(Long recipientId) {
        Friendship friendship = friendshipService.createFriendship(recipientId);

        NotificationCreator notificationCreator = notificationFactory.getNotificationCreator(NotificationType.FRIEND_REQUEST);
        Notification notification = notificationCreator.createNotification(friendship.getResponder(), friendship);
        notificationService.createNotification(notification);
    }

    @Transactional
    public void sendEventInvitationRequest(Long eventId, Long recipientId) {
        Event event = eventService.findEventById(eventId);
        User recipient = userService.findUserById(recipientId);

        NotificationCreator notificationCreator = notificationFactory.getNotificationCreator(NotificationType.EVENT_INVITATION);
        Notification notification = notificationCreator.createNotification(recipient, event);
        notificationService.createNotification(notification);
    }
}

