package sport.app.sport_connecting_people.service.specification;

import org.springframework.data.domain.Pageable;
import sport.app.sport_connecting_people.dto.notification.response.PaginatedNotificationResponseDto;
import sport.app.sport_connecting_people.entity.notification.Notification;

public interface NotificationService {

    PaginatedNotificationResponseDto getUserNotifications(Pageable pageable);

    void createNotification(Notification notification);

    void deleteUserNotifications(Long userId);
}
