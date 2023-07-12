package sport.app.sport_connecting_people.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.event.EventCreationDto;
import sport.app.sport_connecting_people.entity.Event;
import sport.app.sport_connecting_people.mapper.EventMapper;
import sport.app.sport_connecting_people.repository.EventRepository;
import sport.app.sport_connecting_people.repository.UserRepository;

@AllArgsConstructor
@Service
public class EventService {

    private UserRepository userRepository;
    private EventRepository eventRepository;
    private EventMapper eventMapper;

    public void createEvent(EventCreationDto dto) {
        Event event = eventMapper.createEvent(dto);
        eventRepository.save(event);
    }
}
