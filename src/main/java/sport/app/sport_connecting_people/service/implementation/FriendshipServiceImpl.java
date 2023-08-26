package sport.app.sport_connecting_people.service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.user.response.PaginatedUserResponseDto;
import sport.app.sport_connecting_people.dto.user.response.UserResponseDto;
import sport.app.sport_connecting_people.entity.Friendship;
import sport.app.sport_connecting_people.entity.enums.FriendshipStatus;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.entity.notification.FriendRequestNotification;
import sport.app.sport_connecting_people.exceptions.friendship.FriendshipNotFoundException;
import sport.app.sport_connecting_people.exceptions.user.UserNotFoundException;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.FriendshipRepository;
import sport.app.sport_connecting_people.repository.UserRepository;
import sport.app.sport_connecting_people.service.specification.FriendshipService;
import sport.app.sport_connecting_people.service.specification.PrincipalService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class FriendshipServiceImpl implements FriendshipService {

    private FriendshipRepository friendshipRepository;
    private UserRepository userRepository;
    private PrincipalService principalService;
    private UserMapper userMapper;

    @Override
    public Friendship createFriendship(Long responderId) {
        Long requesterId = principalService.getCurrentUserId();

        if (friendshipRepository.existsByRequesterIdAndResponderId(requesterId, responderId)
                || friendshipRepository.existsByRequesterIdAndResponderId(responderId, requesterId)) {
            throw new IllegalStateException("Friendship or request already exists");
        }

        User requester = principalService.getCurrentUser();
        User responder = userRepository.findById(responderId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + responderId));

        Friendship friendship = new Friendship();
        friendship.setRequester(requester);
        friendship.setResponder(responder);
        friendship.setStatus(FriendshipStatus.PENDING);
        friendshipRepository.save(friendship);

        return friendship;
    }

    @Override
    public PaginatedUserResponseDto getUserFriends(String name, Pageable pageable) {
        Long currentUserId = principalService.getCurrentUserId();
        Page<Friendship> friendsPage = friendshipRepository.findAllByRequesterIdAAndAndStatusOrResponderIdAndStatus(currentUserId,
                FriendshipStatus.ACCEPTED,
                currentUserId,
                FriendshipStatus.ACCEPTED,
                pageable);
        List<UserResponseDto> friendsDto = new ArrayList<>();

        for (Friendship friendship : friendsPage.getContent()) {
            UserResponseDto dto;
            if (friendship.getRequester().getId().equals(currentUserId)) {
                dto = userMapper.mapToUserResponseDto(friendship.getResponder());
            } else {
                dto = userMapper.mapToUserResponseDto(friendship.getRequester());
            }
            dto.setStatus(friendship.getStatus());
            friendsDto.add(dto);
        }

        PaginatedUserResponseDto response = new PaginatedUserResponseDto();
        response.setUsers(friendsDto);
        response.setTotalCount(friendsPage.getTotalElements());
        return response;
    }

    @Transactional
    @Override
    public void updateFriendshipStatus(Long friendshipId, boolean status) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new FriendshipNotFoundException("Friendship not found"));

        if (status) {
            friendship.setStatus(FriendshipStatus.ACCEPTED);
        }
        else {
            friendship.setStatus(FriendshipStatus.DECLINED);
        }
        friendshipRepository.save(friendship);
    }

    @Transactional
    @Override
    public void deleteFriendship(Long responderId) {
        Long requesterId = principalService.getCurrentUserId();
        Friendship friendship = friendshipRepository.findByRequesterIdAndResponderId(requesterId, responderId)
                .orElseThrow(() -> new FriendshipNotFoundException("Friendship not found"));

        friendshipRepository.delete(friendship);
    }

    @Override
    public FriendshipStatus getFriendshipStatus(Long currentUserId, Long otherUserId) {
        Optional<Friendship> friendship = friendshipRepository.findByRequesterIdAndResponderId(currentUserId, otherUserId);
        if (friendship.isPresent()) {
            return friendship.get().getStatus();
        } else {
            return FriendshipStatus.NOT_FRIEND;
        }
    }
}

