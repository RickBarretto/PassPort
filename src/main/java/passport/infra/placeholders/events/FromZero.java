package passport.infra.placeholders.events;

import java.time.LocalDate;

import passport.domain.models.events.Event;
import passport.domain.models.events.EventId;
import passport.domain.models.events.Poster;

public class FromZero {
    private final static EventId id = new EventId();

    public static EventId id() { return id; }

    public static String title() { return "From Zero Tour - Linkin Park"; }

    public static LocalDate date() { return LocalDate.of(2024, 11, 15); }

    public static String description() { return """
            Local: Allianz Parque, São Paulo, Brasil

            Junte-se ao Linkin Park no Allianz Parque para uma noite inesquecível como parte da sua turnê From Zero. Este show altamente aguardado apresentará uma mistura dinâmica de seus clássicos e novas músicas do último álbum, "From Zero". Não perca a oportunidade de experimentar a performance eletrizante da banda e testemunhar a evolução musical ao vivo no palco. É uma celebração de música e memórias que você não vai querer perder. 🎤🎸🔥
            Pronto para curtir? 🎶
            """
            .stripIndent(); }

    public static Double price() { return 820.00; }

    public static Event event() {
        return new Event(
                id,
                new Poster(title(), description(), date()),
                price());
    }
}
