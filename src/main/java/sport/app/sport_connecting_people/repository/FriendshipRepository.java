package sport.app.sport_connecting_people.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import sport.app.sport_connecting_people.entity.Friendship;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long>, JpaSpecificationExecutor<Friendship> {

    Optional<Friendship> findByRequesterIdAndResponderId(Long requesterId, Long responderId);

    boolean existsByRequesterIdAndResponderId(Long requesterId, Long responderId);
}
