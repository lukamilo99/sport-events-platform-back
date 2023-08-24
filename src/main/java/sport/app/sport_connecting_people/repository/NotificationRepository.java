package sport.app.sport_connecting_people.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sport.app.sport_connecting_people.entity.notification.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByRecipientId(Long recipientId, Pageable pageable);

    void deleteByRecipientId(Long recipientId);
}


