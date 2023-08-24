package sport.app.sport_connecting_people.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.dto.event.request.EventUpsertDto;
import sport.app.sport_connecting_people.dto.notification.response.NotificationResponseDto;
import sport.app.sport_connecting_people.entity.Event;
import sport.app.sport_connecting_people.entity.notification.EventInvitationNotification;
import sport.app.sport_connecting_people.entity.notification.FriendRequestNotification;
import sport.app.sport_connecting_people.entity.notification.Notification;

@AllArgsConstructor
@Component
public class NotificationMapper {

    public NotificationResponseDto mapToNotificationResponseDto(Notification notification) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(notification.getId());
        dto.setCreationDate(notification.getCreationDate());
        dto.setMessage(notification.getMessage());

        if(notification instanceof FriendRequestNotification) {
            dto.setReferenceId(((FriendRequestNotification) notification).getFriendship().getId());
            dto.setType("FRIEND_REQUEST");
        }
        else if(notification instanceof EventInvitationNotification) {
            dto.setReferenceId(((EventInvitationNotification) notification).getEvent().getId());
            dto.setType("EVENT_JOIN_REQUEST");
        }
        else {
            dto.setReferenceId(null);
        }
        return dto;
    }
}
