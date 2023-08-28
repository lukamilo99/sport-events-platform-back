package sport.app.sport_connecting_people.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.notification.response.NotificationResponseDto;
import sport.app.sport_connecting_people.dto.notification.response.PaginatedNotificationResponseDto;
import sport.app.sport_connecting_people.entity.notification.Notification;
import sport.app.sport_connecting_people.entity.notification.RequestNotification;
import sport.app.sport_connecting_people.entity.request.EventInvitationRequest;
import sport.app.sport_connecting_people.entity.request.FriendshipRequest;
import sport.app.sport_connecting_people.entity.request.Request;
import sport.app.sport_connecting_people.exceptions.notification.NotificationNotFoundException;
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
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public PaginatedNotificationResponseDto getUserNotifications(Pageable pageable) {
        Long userId = principalService.getCurrentUserId();
        Page<Notification> notificationPage = notificationRepository.findByRecipientIdAndIsReadIsFalse(userId, pageable);
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
    public void createNotificationForRequest(Request request) {
        RequestNotification notification = new RequestNotification();

        if (request instanceof EventInvitationRequest eventInvitationRequest) {
            notification = notificationMapper.mapToEventInvitationRequestNotification(eventInvitationRequest);
        }
        else if (request instanceof FriendshipRequest friendshipRequest){
            notification = notificationMapper.mapToFriendRequestNotification(friendshipRequest);
        }
        notificationRepository.save(notification);
        sendNotification(notification);
    }

    @Override
    public void sendNotification(Notification notification) {
        NotificationResponseDto dto = notificationMapper.mapToNotificationResponseDto(notification);
        messagingTemplate.convertAndSend("/topic/notifications", dto);
    }

    @Override
    public void markAsRead(Long requestId) {
        Notification notification = findByRequestId(requestId);
        notification.setRead(true);
    }

    @Override
    public Notification findByRequestId(Long requestId) {
        return notificationRepository.findRequestNotificationByRequestId(requestId)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found"));
    }
}

