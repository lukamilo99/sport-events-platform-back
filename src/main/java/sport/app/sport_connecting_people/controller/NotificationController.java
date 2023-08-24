package sport.app.sport_connecting_people.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sport.app.sport_connecting_people.dto.notification.response.PaginatedNotificationResponseDto;
import sport.app.sport_connecting_people.service.specification.NotificationService;

@AllArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private NotificationService notificationService;

    @GetMapping()
    public ResponseEntity<PaginatedNotificationResponseDto> getUserNotifications(@RequestParam int page,
                                                                                 @RequestParam(defaultValue = "6") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(notificationService.getUserNotifications(pageable), HttpStatus.OK);
    }
}
