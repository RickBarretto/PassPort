package passport.infra.placeholders.events;

import java.time.LocalDate;

import passport.domain.models.events.Event;
import passport.domain.models.events.EventId;
import passport.domain.models.events.Poster;

public class Ballerina {
    static EventId id = new EventId();

    public static EventId id() { return id; }

    public static String title() { return "Ballerina"; }

    public static String description() { return """
            Local: Cinema Premium, Rua Oscar Freire, 1000, São Paulo, SP

            Sinopse: "Ballerina" é um spin-off da franquia "John Wick" e segue Eve Macarro (Ana de Armas), uma jovem assassina que busca vingança pela morte de sua família. Durante os eventos de "John Wick: Capítulo 3 – Parabellum", Eve começa a treinar nas tradições assassinas dos Ruska Roma e se prepara para enfrentar seus inimigos1. O filme é cheio de ação intensa, coreografias de luta impressionantes e uma trama envolvente, prometendo ser uma experiência cinematográfica inesquecível.

            Está pronto para uma noite de adrenalina no Cinema Premium? 🎬🎥
            """; }

    public static LocalDate date() { return LocalDate.of(2025, 6, 6); }

    public static Double price() { return 30.00; }

    public static Event event() {
        return new Event(
                id,
                new Poster(title(), description(), date()),
                price());
    }

}
