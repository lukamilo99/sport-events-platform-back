package sport.app.sport_connecting_people.service.specification;

import org.springframework.data.domain.Pageable;
import sport.app.sport_connecting_people.dto.notification.response.PaginatedNotificationResponseDto;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.entity.notification.Notification;
import sport.app.sport_connecting_people.entity.request.Request;

public interface NotificationService {

    PaginatedNotificationResponseDto getUserNotifications(Pageable pageable);

    void createNotificationForRequest(Request request);

    void sendNotification(Notification notification);

    void markAsRead(Long requestId);

    Notification findByRequestId(Long requestId);
}
