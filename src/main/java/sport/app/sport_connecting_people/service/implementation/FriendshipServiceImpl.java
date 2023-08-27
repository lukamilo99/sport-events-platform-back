package sport.app.sport_connecting_people.service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.user.response.PaginatedUserResponseDto;
import sport.app.sport_connecting_people.dto.user.response.UserResponseDto;
import sport.app.sport_connecting_people.entity.Friendship;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.exceptions.friendship.FriendshipNotFoundException;
import sport.app.sport_connecting_people.mapper.FriendshipMapper;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.FriendshipRepository;
import sport.app.sport_connecting_people.service.specification.FriendshipService;
import sport.app.sport_connecting_people.service.specification.PrincipalService;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class FriendshipServiceImpl implements FriendshipService {

    private FriendshipRepository friendshipRepository;
    private PrincipalService principalService;
    private FriendshipMapper friendshipMapper;
    private UserMapper userMapper;

    @Override
    public void createFriendship(User requester, User responder) {
        if (friendshipRepository.existsByRequesterAndResponder(requester, responder)
                || friendshipRepository.existsByRequesterAndResponder(responder, requester)) {
            throw new IllegalStateException("Friendship or request already exists");
        }

        Friendship friendship = friendshipMapper.mapToFriendship(requester, responder);
        friendshipRepository.save(friendship);
    }

    @Override
    public PaginatedUserResponseDto getUserFriends(String name, Pageable pageable) {
        User requester = principalService.getCurrentUser();

        Page<Friendship> friendsPage = friendshipRepository.findAllByRequesterOrResponder(requester, requester, pageable);
        List<UserResponseDto> friendsDto = new ArrayList<>();

        for (Friendship friendship : friendsPage.getContent()) {
            UserResponseDto dto;
            if (friendship.getRequester().getId().equals(requester.getId())) {
                dto = userMapper.mapToUserResponseDto(friendship.getResponder());
            } else {
                dto = userMapper.mapToUserResponseDto(friendship.getRequester());
            }
            friendsDto.add(dto);
        }

        PaginatedUserResponseDto response = new PaginatedUserResponseDto();
        response.setUsers(friendsDto);
        response.setTotalCount(friendsPage.getTotalElements());
        return response;
    }

    @Transactional
    @Override
    public void deleteFriendship(Long responderId) {
        Long requesterId = principalService.getCurrentUserId();
        Friendship friendship = friendshipRepository.findByRequesterIdAndResponderId(requesterId, responderId)
                .orElseThrow(() -> new FriendshipNotFoundException("Friendship not found"));

        friendshipRepository.delete(friendship);
    }
}

