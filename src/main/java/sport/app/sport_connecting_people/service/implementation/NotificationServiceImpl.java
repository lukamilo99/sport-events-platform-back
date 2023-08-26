package sport.app.sport_connecting_people.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.notification.response.NotificationResponseDto;
import sport.app.sport_connecting_people.dto.notification.response.PaginatedNotificationResponseDto;
import sport.app.sport_connecting_people.entity.notification.Notification;
import sport.app.sport_connecting_people.mapper.NotificationMapper;
import sport.app.sport_connecting_people.repository.NotificationRepository;
import sport.app.sport_connecting_people.service.specification.NotificationService;
import sport.app.sport_connecting_people.service.specification.PrincipalService;

import java.util.List;

@AllArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;
    private PrincipalService principalService;
    private NotificationMapper notificationMapper;

    @Override
    public PaginatedNotificationResponseDto getUserNotifications(Pageable pageable) {
        Long userId = principalService.getCurrentUserId();
        Page<Notification> notificationPage = notificationRepository.findByRecipientId(userId, pageable);
        PaginatedNotificationResponseDto response = new PaginatedNotificationResponseDto();

        if(!notificationPage.isEmpty()) {
            List<NotificationResponseDto> notificationResponseDtos = notificationPage.getContent().stream()
                    .map(notification -> notificationMapper.mapToNotificationResponseDto(notification))
                    .toList();

            response.setNotifications(notificationResponseDtos);
            response.setTotalCount(notificationPage.getTotalElements());
        } else {
            response.setNotifications(List.of());
            response.setTotalCount(0L);
            response.setUnreadNotificationsCount(0L);
        }
        return response;
    }

    @Override
    public void createNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public void deleteUserNotifications(Long userId) {
        notificationRepository.deleteByRecipientId(userId);
    }
}

