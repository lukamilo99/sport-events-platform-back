package sport.app.sport_connecting_people.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sport.app.sport_connecting_people.entity.enums.FriendshipStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseDto {

    private Long id;
    private String name;
    private FriendshipStatus status;
}
