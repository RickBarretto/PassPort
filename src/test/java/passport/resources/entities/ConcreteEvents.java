package passport.resources.entities;

import java.time.LocalDate;
import java.util.List;

import passport.domain.models.events.Event;
import passport.domain.models.events.Poster;
import passport.infra.virtual.EventsInMemory;

public class ConcreteEvents {
    private static final Event fromZero = new Event(new Poster("From Zero Tour",
            "LP show", LocalDate.of(2024, 11, 15)));

    private static final Event extraFromZero = new Event(
            new Poster("From Zero (Extra show)", "Extra show of LP",
                    LocalDate.of(2024, 11, 16)));

    public static Event FromZeroTour() { return fromZero; }

    public static Event ExtraFromZeroTour() { return extraFromZero; }

    public static EventsInMemory empty() { return new EventsInMemory(); }

    public static EventsInMemory withFromZero() {
        return new EventsInMemory(List.of(fromZero));
    }

}
