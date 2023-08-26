package sport.app.sport_connecting_people.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sport.app.sport_connecting_people.entity.Friendship;
import sport.app.sport_connecting_people.entity.enums.FriendshipStatus;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Page<Friendship> findAllByRequesterIdAAndAndStatusOrResponderIdAndStatus(Long requesterId, FriendshipStatus status1, Long responderId, FriendshipStatus status2, Pageable pageable);

    Optional<Friendship> findByRequesterIdAndResponderId(Long requesterId, Long responderId);

    boolean existsByRequesterIdAndResponderId(Long requesterId, Long responderId);
}
