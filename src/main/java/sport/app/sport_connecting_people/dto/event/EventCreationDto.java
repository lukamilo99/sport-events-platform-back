package sport.app.sport_connecting_people.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventCreationDto {

    @NotBlank
    private String name;
    @NotBlank
    private Integer capacity;
    @NotBlank
    private String sport;
    @NotBlank
    private String location;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Date date;
}
