package sport.app.sport_connecting_people.dto.notification.response;

import lombok.*;
import sport.app.sport_connecting_people.entity.enums.NotificationType;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class NotificationResponseDto {

    private Long id;

    private NotificationType type;

    private LocalDateTime creationDate;

    private String message;

    // ovo ce biti id entiteta u zavisnosti od tipa obavestenja
    private Long referenceId;
}
