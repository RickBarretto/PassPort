package passport.infra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import passport.domain.models.evaluations.Evaluation;
import passport.domain.models.users.UserId;
import passport.infra.json.EventsJson;
import passport.infra.json.JsonFile;
import passport.resources.bdd.Then;
import passport.resources.entities.ConcreteEvents;

public class EventsJsonTest {
    private static final String directory = "src/test/java/passport/infra/resources";

    @AfterAll
    static void removeFiles() {
        new JsonFile(directory, "all-events").delete();
        new JsonFile(directory, "all-evaluated-events").delete();
        new JsonFile(directory, "none-events").delete();
    }

    @Nested
    class ExistentRepository {
        private final JsonFile file = new JsonFile(directory, "all-events");
        private EventsJson allEventsJson;

        @BeforeEach
        void createJsonFile() {
            new EventsJson(file, ConcreteEvents.withFromZero());
        }

        @BeforeEach
        void loadJsonFile() {
            assumeTrue(file.exists());
            this.allEventsJson = new EventsJson(file);
        }

        @Test
        void shouldContainEvent() {
            final var expectedTitle = ConcreteEvents.FromZeroTour().poster()
                    .title();
            final var expectedDate = ConcreteEvents.FromZeroTour().poster()
                    .date();

            assertTrue(1 == this.allEventsJson.list().size());
            assertTrue(this.allEventsJson.has(expectedTitle, expectedDate));
            assertTrue(this.allEventsJson.event(expectedTitle, expectedDate)
                    .isPresent());
        }

        @Test
        void shouldRegister() {
            registerExtraShow();

            final var expectedTitle = ConcreteEvents.ExtraFromZeroTour()
                    .poster().title();
            final var expectedDate = ConcreteEvents.ExtraFromZeroTour().poster()
                    .date();

            assertTrue(this.allEventsJson.has(expectedTitle, expectedDate));
            assertTrue(this.allEventsJson.event(expectedTitle, expectedDate)
                    .isPresent());
        }

        @Test
        @Then("Should load list from file")
        void shouldLoadFromFile() {
            registerExtraShow();

            final var expectedTitle = ConcreteEvents.ExtraFromZeroTour()
                    .poster().title();
            final var expectedDate = ConcreteEvents.ExtraFromZeroTour().poster()
                    .date();

            var otherReference = new EventsJson(file);
            assertTrue(otherReference.event(expectedTitle, expectedDate)
                    .isPresent());
        }

        void registerExtraShow() {
            this.allEventsJson.register(ConcreteEvents.ExtraFromZeroTour());
        }
    }

    @Nested
    class EmptyRepository {
        private final JsonFile file = new JsonFile(directory, "none-events");
        private EventsJson noEvents;

        @BeforeEach
        void createJsonFile() { new EventsJson(file, ConcreteEvents.empty()); }

        @BeforeEach
        void loadJsonFile() {
            assumeTrue(file.exists());
            this.noEvents = new EventsJson(file);
        }

        @Test
        @Then("Internal list should be empty")
        void shouldBeEmpty() { assertTrue(this.noEvents.list().isEmpty()); }

        @Test
        @Then("Should load an empty list from the json file")
        void shouldLoadEmptyListFromFile() {
            var otherReference = new EventsJson(file);
            assertTrue(otherReference.list().isEmpty());
        }
    }

    @Nested
    class RepositoryOfEvaluatedEvents {
        private final JsonFile file = new JsonFile(directory,
                "all-evaluated-events");
        private EventsJson events;

        @BeforeEach
        void createJsonFile() {
            new EventsJson(file, ConcreteEvents.withFromZero());
        }

        @BeforeEach
        void loadJsonFile() {
            assumeTrue(file.exists());
            this.events = new EventsJson(file);
        }

        @Test
        void shouldUpdate() {
            final var author = new UserId();
            final var event = ConcreteEvents.FromZeroTour();

            event.receiveEvaluation(
                    new Evaluation(event.id(), author, "Good show!"));
            events.update(event);

            var newEvents = new EventsJson(file);
            var storedEvaluation = newEvents.byId(event.id()).get()
                    .evaluations().get(0);
            assertAll("Evaluation was stored",
                    () -> assertEquals("Good show!",
                            storedEvaluation.comment()),
                    () -> assertEquals(author, storedEvaluation.author()),
                    () -> assertEquals(event.id(), storedEvaluation.event()));
        }
    }
}
