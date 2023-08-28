package sport.app.sport_connecting_people.service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.relation.RelationDto;
import sport.app.sport_connecting_people.dto.user.response.PaginatedUserResponseDto;
import sport.app.sport_connecting_people.dto.user.response.UserResponseDto;
import sport.app.sport_connecting_people.entity.Friendship;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.entity.enums.RequestStatus;
import sport.app.sport_connecting_people.mapper.FriendshipMapper;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.FriendshipRepository;
import sport.app.sport_connecting_people.service.specification.FriendshipService;
import sport.app.sport_connecting_people.service.specification.PrincipalService;
import sport.app.sport_connecting_people.specification.FriendshipSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FriendshipServiceImpl implements FriendshipService {

    private FriendshipRepository friendshipRepository;
    private PrincipalService principalService;
    private FriendshipMapper friendshipMapper;
    private UserMapper userMapper;

    @Override
    public void createFriendship(User requester, User responder) {
        if (friendshipRepository.existsByRequesterIdAndResponderId(requester.getId(), responder.getId())
                || friendshipRepository.existsByRequesterIdAndResponderId(responder.getId(), requester.getId())) {
            throw new IllegalStateException("Friendship or request already exists");
        }

        Friendship friendship = friendshipMapper.mapToFriendship(requester, responder);
        friendshipRepository.save(friendship);
    }

    @Override
    public PaginatedUserResponseDto getUserFriends(String name, Pageable pageable) {
        if (name == null) {
            PaginatedUserResponseDto response = new PaginatedUserResponseDto();
            response.setUsers(new ArrayList<>());
            response.setTotalCount(0);
            return response;
        }

        User requester = principalService.getCurrentUser();
        Specification<Friendship> spec = Specification
                .where(FriendshipSpecification.hasRequesterOrResponderId(requester.getId()))
                .and(FriendshipSpecification.hasRequesterOrResponderName(name));

        Page<Friendship> friendsPage = friendshipRepository.findAll(spec, pageable);
        List<UserResponseDto> friendsDto = friendsPage.getContent().stream()
                .map(friendship -> createUserResponseDtoFromFriendship(friendship, requester))
                .collect(Collectors.toList());

        PaginatedUserResponseDto response = new PaginatedUserResponseDto();
        response.setUsers(friendsDto);
        response.setTotalCount(friendsPage.getTotalElements());
        return response;
    }


    @Override
    public Friendship findByRequesterAndResponder(User requester, User responder) {
        return friendshipRepository.findByRequesterIdAndResponderId(requester.getId(), responder.getId())
                .orElse(null);
    }

    @Override
    public Friendship findById(Long friendshipId) {
        return friendshipRepository.findById(friendshipId)
                .orElse(null);
    }

    @Transactional
    @Override
    public void deleteFriendship(Long friendshipId) {
        friendshipRepository.deleteById(friendshipId);
    }

    private UserResponseDto createUserResponseDtoFromFriendship(Friendship friendship, User requester) {
        User friend = friendship.getRequester().getId().equals(requester.getId()) ? friendship.getResponder() : friendship.getRequester();
        UserResponseDto dto = userMapper.mapToUserResponseDto(friend);
        RelationDto relationDto = createRelationDtoFromFriendship(friendship);
        dto.setRelation(relationDto);
        return dto;
    }

    private RelationDto createRelationDtoFromFriendship(Friendship friendship) {
        RelationDto relationDto = new RelationDto();
        relationDto.setFriendshipId(friendship.getId());
        relationDto.setStatus(RequestStatus.ACCEPTED);
        return relationDto;
    }
}

