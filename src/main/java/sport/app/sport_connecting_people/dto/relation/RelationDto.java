package sport.app.sport_connecting_people.dto.relation;

import lombok.Getter;
import lombok.Setter;
import sport.app.sport_connecting_people.entity.enums.RequestStatus;

@Getter
@Setter
public class RelationDto {

    private  Long friendshipId;

    private Long requestId;

    private Long senderId;

    private Long recipientId;

    private RequestStatus status;
}
