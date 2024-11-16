package passport.infra.placeholders.events;

import java.time.LocalDate;

import passport.domain.models.events.Event;
import passport.domain.models.events.EventId;
import passport.domain.models.events.Poster;

public class RockInRio {
    private static EventId id = new EventId();

    public static EventId id() { return id; }

    public static String title() { return "Rock in Rio 2025"; }

    public static LocalDate date() { return LocalDate.of(2025, 11, 8); }

    public static String description() { return """
                O Rock in Rio é um dos maiores e mais icônicos festivais de música do mundo, realizado na vibrante cidade do Rio de Janeiro, Brasil. Celebrado desde 1985, o festival reúne artistas renomados e emergentes de diversos gêneros musicais, proporcionando experiências inesquecíveis para fãs de todas as idades.

                Destaques do Festival:
                * Line-up Diversificado: Espere apresentações épicas de artistas internacionais e nacionais, abrangendo rock, pop, eletrônica e muito mais.
                * Palcos Temáticos: Diversos palcos com ambientes únicos, cada um oferecendo uma atmosfera distinta e memorável.
                * Experiências Interativas: Além dos shows, o festival oferece uma variedade de atividades interativas, incluindo tirolesa, roda-gigante e áreas gastronômicas com comidas deliciosas de todo o mundo.
                * Sustentabilidade: Rock in Rio também é conhecido por suas iniciativas de sustentabilidade, promovendo práticas ecológicas e responsabilidade social.

                Prepare-se para uma jornada musical extraordinária no Rock in Rio 2025, onde a magia da música e a paixão dos fãs se unem para criar um evento verdadeiramente espetacular. 🎸🎤🌟
            """; }

    public static Double price() { return 795.50; }

    public static Event event() {
        return new Event(
                id,
                new Poster(title(), description(), date()),
                price());
    }

}
