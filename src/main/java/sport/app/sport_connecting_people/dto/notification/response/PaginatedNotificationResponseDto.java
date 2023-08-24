package sport.app.sport_connecting_people.dto.notification.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginatedNotificationResponseDto {

    private List<NotificationResponseDto> notifications;

    private Long totalCount;

    private Long unreadNotificationsCount;
}
