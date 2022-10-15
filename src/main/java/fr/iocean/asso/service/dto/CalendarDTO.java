package fr.iocean.asso.service.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CalendarDTO implements Serializable {

    private static final long serialVersionUID = 5168356831380171385L;

    private List<EventDTO> events;
}
