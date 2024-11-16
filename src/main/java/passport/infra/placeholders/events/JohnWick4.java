package passport.infra.placeholders.events;

import java.time.LocalDate;

import passport.domain.models.events.Event;
import passport.domain.models.events.EventId;
import passport.domain.models.events.Poster;

public class JohnWick4 {
    static EventId id = new EventId();

    public static EventId id() { return id; }

    public static String title() { return "John Wick: Capítulo 4"; }

    public static String description() { return """
            Local: Cinema Central, Avenida Paulista, 1234, São Paulo, SP

            Sinopse: John Wick (Keanu Reeves) retorna para enfrentar seus maiores desafios até agora. Com uma recompensa crescente por sua cabeça, ele precisa lutar contra os assassinos mais letais do submundo enquanto busca vingança contra a Alta Cúpula. Repleto de ação intensa, coreografias de luta impressionantes e uma trama de tirar o fôlego, John Wick: Capítulo 4 promete ser uma experiência cinematográfica inesquecível.

            Esteja preparado para uma noite eletrizante no Cinema Central! 🍿🎬
            """
            .stripIndent(); }

    public static LocalDate date() { return LocalDate.of(2023, 3, 24); }

    public static Double price() { return 30.00; }

    public static Event event() {
        return new Event(
                id,
                new Poster(title(), description(), date()),
                price());
    }

}
