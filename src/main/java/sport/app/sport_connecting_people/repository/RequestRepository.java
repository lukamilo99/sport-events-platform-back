package sport.app.sport_connecting_people.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sport.app.sport_connecting_people.entity.request.Request;

import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("SELECT r FROM Request r WHERE TYPE(r) = FriendshipRequest " +
            "AND ((r.sender.id = :currentUserId AND r.receiver.id = :otherUserId) OR (r.sender.id = :otherUserId AND r.receiver.id = :currentUserId))")
    Optional<Request> findFriendshipRequestForIds(@Param("currentUserId") Long currentUserId, @Param("otherUserId") Long otherUserId);

}
