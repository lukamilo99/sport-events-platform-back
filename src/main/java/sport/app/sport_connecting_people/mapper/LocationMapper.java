package sport.app.sport_connecting_people.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.dto.location.LocationResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Component
public class LocationMapper {

    private ObjectMapper objectMapper;

    public List<LocationResponse> createLocationResponseList(Object response) {
        JsonNode rootNode = objectMapper.convertValue(response, JsonNode.class);
        JsonNode features = rootNode.get("features");

        if (features == null) {
            return Collections.emptyList();
        }

        List<LocationResponse> responseList = new ArrayList<>();
        for (JsonNode feature : features) {
            LocationResponse locationResponse = extractLocationResponseFromFeature(feature);
            if (locationResponse != null) {
                responseList.add(locationResponse);
            }
        }
        return responseList;
    }

    private LocationResponse extractLocationResponseFromFeature(JsonNode featureNode) {
        JsonNode properties = featureNode.get("properties");
        if (properties == null) {
            return null;
        }

        String formatted = properties.get("formatted").textValue();
        double lon = properties.get("lon").asDouble();
        double lat = properties.get("lat").asDouble();

        return new LocationResponse(formatted, List.of(lat, lon));
    }
}
