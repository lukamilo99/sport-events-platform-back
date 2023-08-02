package sport.app.sport_connecting_people.dto.user.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginatedUserProfileDto {

    private List<UserProfileDto> users;
    private long totalCount;
}
