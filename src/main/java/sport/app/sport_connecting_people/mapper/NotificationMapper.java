package sport.app.sport_connecting_people.mapper;

import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.dto.notification.response.NotificationResponseDto;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.entity.enums.NotificationType;
import sport.app.sport_connecting_people.entity.notification.Notification;
import sport.app.sport_connecting_people.entity.notification.RequestNotification;
import sport.app.sport_connecting_people.entity.request.EventInvitationRequest;
import sport.app.sport_connecting_people.entity.request.FriendshipRequest;
import sport.app.sport_connecting_people.entity.request.Request;

import java.time.LocalDateTime;

@Component
public class NotificationMapper {

    public RequestNotification mapToFriendRequestNotification(FriendshipRequest request) {
        RequestNotification notification = new RequestNotification();
        notification.setMessage(request.getSender().getFirstname() + " " + request.getSender().getLastname() + " has sent you a friend request!");
        notification.setCreationDate(LocalDateTime.now());
        notification.setRequest(request);
        notification.setRecipient(request.getReceiver());
        return notification;
    }

    public RequestNotification mapToEventInvitationRequestNotification(EventInvitationRequest request) {
        RequestNotification notification = new RequestNotification();
        notification.setMessage(request.getSender().getFirstname() + " " + request.getSender().getLastname() + " has invited you to an event!");
        notification.setCreationDate(LocalDateTime.now());
        notification.setRequest(request);
        notification.setRecipient(request.getReceiver());
        return notification;
    }

    public NotificationResponseDto mapToNotificationResponseDto(Notification notification) {
        NotificationResponseDto dto = new NotificationResponseDto();

        if (notification instanceof RequestNotification requestNotification) {
            dto.setRequestId(requestNotification.getRequest().getId());
            dto.setType(NotificationType.REQUEST);
        }
        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());
        dto.setCreationDate(dto.getCreationDate());

        return dto;
    }
}
