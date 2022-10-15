package fr.iocean.asso.web.rest;

import fr.iocean.asso.service.dto.CalendarDTO;
import fr.iocean.asso.service.dto.EventDTO;
import fr.iocean.asso.service.dto.ExtendedProps;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CalendarResource {

    private final Logger log = LoggerFactory.getLogger(CalendarResource.class);

    @GetMapping("/calendar")
    public CalendarDTO createChat(
        @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate start,
        @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate end,
        @RequestParam("login") String login
    ) {
        log.info("Get event for: {}, from: {}, to: {}", login, start, end);

        CalendarDTO calendar = new CalendarDTO();
        List<EventDTO> events = new ArrayList<>();

        EventDTO event1 = new EventDTO();
        event1.setStart(LocalDate.of(2022, 10, 14));
        event1.setTitle("Rappel primo vaccination timonazeaze");
        event1.setBackgroundColor("orange");
        ExtendedProps extendedProps = new ExtendedProps();
        extendedProps.setEntity("Chat");
        extendedProps.setId(1l);
        event1.setExtendedProps(extendedProps);
        events.add(event1);

        EventDTO event2 = new EventDTO();
        event2.setStart(LocalDate.of(2022, 10, 14));
        event2.setEnd(LocalDate.of(2022, 10, 19));
        event2.setTitle("Absence nourrisage 1");
        event2.setBackgroundColor("red");
        ExtendedProps extendedProps2 = new ExtendedProps();
        extendedProps2.setEntity("Absence");
        extendedProps2.setId(1l);
        event2.setExtendedProps(extendedProps);
        events.add(event2);

        EventDTO event3 = new EventDTO();
        event3.setStart(LocalDate.of(2022, 10, ThreadLocalRandom.current().nextInt(1, 30)));
        event3.setTitle("Rappel castration");
        event3.setBackgroundColor("pink");
        ExtendedProps extendedProps3 = new ExtendedProps();
        extendedProps3.setEntity("Chat");
        extendedProps3.setId(1l);
        event3.setExtendedProps(extendedProps);
        events.add(event3);

        calendar.setEvents(events);
        return calendar;
    }
}
