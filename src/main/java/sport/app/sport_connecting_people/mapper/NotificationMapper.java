package sport.app.sport_connecting_people.mapper;

import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.dto.notification.response.NotificationResponseDto;
import sport.app.sport_connecting_people.entity.Event;
import sport.app.sport_connecting_people.entity.Friendship;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.entity.enums.NotificationType;
import sport.app.sport_connecting_people.entity.notification.EventInvitationNotification;
import sport.app.sport_connecting_people.entity.notification.FriendRequestNotification;
import sport.app.sport_connecting_people.entity.notification.Notification;

import java.time.LocalDateTime;

@Component
public class NotificationMapper {

    public NotificationResponseDto mapToNotificationResponseDto(Notification notification) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(notification.getId());
        dto.setCreationDate(notification.getCreationDate());
        dto.setMessage(notification.getMessage());

        if(notification instanceof FriendRequestNotification) {
            dto.setReferenceId(((FriendRequestNotification) notification).getFriendship().getId());
            dto.setType(NotificationType.FRIEND_REQUEST);
        }
        else if(notification instanceof EventInvitationNotification) {
            dto.setReferenceId(((EventInvitationNotification) notification).getEvent().getId());
            dto.setType(NotificationType.EVENT_INVITATION);
        }
        else {
            dto.setReferenceId(null);
        }
        return dto;
    }

    public FriendRequestNotification mapToFriendRequestNotification(User recipient, Friendship friendship) {
        User requester = friendship.getRequester();
        FriendRequestNotification notification = new FriendRequestNotification();
        notification.setMessage(requester.getFirstname() + " " + requester.getLastname() + " has sent you a friend request!");
        notification.setCreationDate(LocalDateTime.now());
        notification.setFriendship(friendship);
        notification.setRecipient(recipient);
        return notification;
    }

    public EventInvitationNotification mapToEventInvitationNotification(User recipient, Event event) {
        EventInvitationNotification notification = new EventInvitationNotification();
        notification.setMessage("You have been invited to " + event.getName() + " event!");
        notification.setCreationDate(LocalDateTime.now());
        notification.setEvent(event);
        notification.setRecipient(recipient);
        return notification;
    }
}
