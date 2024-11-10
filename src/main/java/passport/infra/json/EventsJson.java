package passport.infra.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import passport.domain.models.events.Event;
import passport.domain.models.events.EventId;
import passport.infra.virtual.EventsInMemory;
import passport.roles.repositories.Events;

/**
 * Implementation of the Events repository using JSON for persistence. It
 * integrates with an in-memory repository (EventsInMemory) and handles JSON
 * serialization/deserialization for persistence.
 */
public class EventsJson implements Events {
    private final JsonFile file;
    private EventsInMemory events;

    /**
     * Constructor initializing the repository with a file path.
     *
     * @param filepath the path to the JSON file
     */
    public EventsJson(JsonFile filepath) {
        this(filepath, new EventsInMemory(load(filepath)));
    }

    /**
     * Constructor initializing the repository with a file and in-memory
     * repository.
     *
     * @param file       the JSON file
     * @param repository the in-memory events repository
     */
    public EventsJson(JsonFile file, EventsInMemory repository) {
        this.file = file;
        this.events = repository;
        persist();
    }

    /**
     * Loads events from a JSON file.
     *
     * @param file the JSON file
     * @return a list of events
     */
    private static List<Event> load(JsonFile file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return new Gson().fromJson(reader,
                    new TypeToken<List<Event>>() {}.getType());
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Persists the current state of the in-memory repository to a JSON file.
     */
    private void persist() {
        try (FileWriter writer = new FileWriter(file)) {
            new Gson().toJson(events.list(), writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Event> list() { return events.list(); }

    @Override
    public List<Event> availableOn(LocalDate date) {
        return events.availableOn(date);
    }

    @Override
    public Optional<Event> byId(EventId id) { return events.byId(id); }

    @Override
    public Optional<Event> event(String title, LocalDate date) {
        return events.event(title, date);
    }

    @Override
    public boolean has(EventId id) { return events.has(id); }

    @Override
    public boolean has(String title, LocalDate inDate) {
        return events.has(title, inDate);
    }

    @Override
    public void register(Event event) {
        events.register(event);
        persist();
    }

    @Override
    public void update(Event event) {
        events.update(event);
        persist();
    }
}
