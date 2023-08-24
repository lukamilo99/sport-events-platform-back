package sport.app.sport_connecting_people.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.dto.location.response.LocationResponseDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Component
public class LocationMapper {

    private ObjectMapper objectMapper;

    public List<LocationResponseDto> createLocationResponseList(Object response) {
        JsonNode rootNode = objectMapper.convertValue(response, JsonNode.class);
        JsonNode features = rootNode.get("features");

        if (features == null) {
            return Collections.emptyList();
        }

        List<LocationResponseDto> responseList = new ArrayList<>();
        for (JsonNode feature : features) {
            LocationResponseDto locationResponseDto = extractLocationResponseFromFeature(feature);
            if (locationResponseDto != null) {
                responseList.add(locationResponseDto);
            }
        }
        return responseList;
    }

    private LocationResponseDto extractLocationResponseFromFeature(JsonNode featureNode) {
        JsonNode properties = featureNode.get("properties");
        if (properties == null) {
            return null;
        }

        String city = properties.get("city").textValue();
        String formatted = properties.get("formatted").textValue();
        double lon = properties.get("lon").asDouble();
        double lat = properties.get("lat").asDouble();

        return new LocationResponseDto(city, formatted, List.of(lat, lon));
    }
}
