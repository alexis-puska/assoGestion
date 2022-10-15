package fr.iocean.asso.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventDTO implements Serializable {

    private static final long serialVersionUID = 103945077554792489L;
    private LocalDate start;
    private LocalDate end;
    private String title;
    private String backgroundColor;
    private ExtendedProps extendedProps;
}
