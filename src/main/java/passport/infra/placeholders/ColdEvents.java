package passport.infra.placeholders;

import java.util.List;

import passport.domain.models.events.Event;
import passport.infra.placeholders.events.*;

public class ColdEvents {
    public static final List<Event> list = List.of(
            FromZero.event(),
            RockInRio.event(),
            JohnWick4.event(),
            Ballerina.event());
}