package sport.app.sport_connecting_people.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sport.app.sport_connecting_people.entity.enums.RequestStatus;
import sport.app.sport_connecting_people.entity.request.Request;

import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("SELECT r FROM Request r WHERE TYPE(r) = FriendshipRequest AND r.receiver.id = :receiverId AND r.status = :status")
    Optional<Request> findFriendshipRequestByReceiverIdAndStatus(@Param("receiverId") Long receiverId, @Param("status") RequestStatus status);
}
