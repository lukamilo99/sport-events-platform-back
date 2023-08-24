package sport.app.sport_connecting_people.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sport.app.sport_connecting_people.dto.user.response.UserResponseDto;
import sport.app.sport_connecting_people.entity.User;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.isEnabled = :isEnabled WHERE u.id = :userId")
    int updateUserEnabledStatus(@Param("userId") Long userId, @Param("isEnabled") boolean isEnabled);
}
