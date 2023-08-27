package sport.app.sport_connecting_people.dto.notification.response;

import lombok.*;
import sport.app.sport_connecting_people.entity.enums.NotificationType;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationResponseDto {

    private Long id;
    private Long requestId;
    private NotificationType type;
    private String message;
    private LocalDateTime creationDate;
}
