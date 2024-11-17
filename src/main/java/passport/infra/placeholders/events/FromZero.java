package passport.infra.placeholders.events;

import java.time.LocalDate;
import java.util.List;

import passport.domain.models.evaluations.Evaluation;
import passport.domain.models.events.BoxOffice;
import passport.domain.models.events.Event;
import passport.domain.models.events.EventId;
import passport.domain.models.events.Poster;
import passport.domain.models.events.Ticket;
import passport.infra.placeholders.users.RickAdm;
import passport.infra.placeholders.users.RickB;

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

    public static Poster poster() {
        return new Poster(title(), description(), date());
    }

    public static List<Evaluation> evaluations() {
        return List.of(
                new Evaluation(id, RickAdm.id(),
                        "Two Faced é minha música preferida!"),
                new Evaluation(id, RickB.id(),
                        "Show incrível! #WelcomeBackToBrazil!"));
    }

    public static BoxOffice boxOffice() {
        return new BoxOffice(ticket(), 45000, 45000);
    }

    public static Ticket ticket() { return new Ticket(id, price()); }

    public static Event event() {
        return new Event(id, poster(), boxOffice(), evaluations());
    }
}
