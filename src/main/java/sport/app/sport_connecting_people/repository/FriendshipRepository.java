package sport.app.sport_connecting_people.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sport.app.sport_connecting_people.entity.Friendship;
import sport.app.sport_connecting_people.entity.User;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Page<Friendship> findAllByRequesterOrResponder(User requester, User responder, Pageable pageable);

    Optional<Friendship> findByRequesterIdAndResponderId(Long requesterId, Long responderId);

    boolean existsByRequesterAndResponder(User requester, User responder);
}
