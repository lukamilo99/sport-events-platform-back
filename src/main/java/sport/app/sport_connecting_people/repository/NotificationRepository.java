package sport.app.sport_connecting_people.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sport.app.sport_connecting_people.entity.notification.Notification;
import sport.app.sport_connecting_people.entity.notification.RequestNotification;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT rn FROM RequestNotification rn WHERE rn.request.id = :requestId")
    Optional<RequestNotification> findRequestNotificationByRequestId(@Param("requestId") Long requestId);

    Page<Notification> findByRecipientIdAndIsReadIsFalse(Long recipientId, Pageable pageable);
}


