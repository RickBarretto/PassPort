package passport.infra.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import passport.domain.models.users.Login;
import passport.domain.models.users.User;
import passport.domain.models.users.UserId;
import passport.domain.models.users.values.EmailAddress;
import passport.infra.virtual.UsersInMemory;
import passport.roles.repositories.Users;

/**
 * Implementation of the Users repository using JSON for persistence. It
 * integrates with an in-memory repository (UsersInMemory) and handles JSON
 * serialization/deserialization for persistence.
 */
public class UsersJson implements Users {
    private final JsonFile file;
    private UsersInMemory users;

    /**
     * Constructor initializing the repository with a file path.
     *
     * @param filepath the path to the JSON file
     */
    public UsersJson(JsonFile filepath) {
        this(filepath, new UsersInMemory(load(filepath)));
    }

    /**
     * Constructor initializing the repository with a file and in-memory
     * repository.
     *
     * @param filepath   the JSON file
     * @param repository the in-memory users repository
     */
    public UsersJson(JsonFile filepath, UsersInMemory repository) {
        this.file = filepath;
        this.users = repository;
        persist();
    }

    /**
     * Loads users from a JSON file.
     *
     * @param filepath the JSON file
     * @return a list of users
     */
    private static List<User> load(JsonFile filepath) {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(filepath))) {
            return new Gson().fromJson(reader,
                    new TypeToken<List<User>>() {}.getType());
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
            new Gson().toJson(users.list(), writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(User user) {
        users.register(user);
        persist();
    }

    @Override
    public void update(User target, User newUser) {
        users.update(target, newUser);
        persist();
    }

    @Override
    public Optional<User> byId(UserId id) { return users.byId(id); }

    @Override
    public Optional<User> ownerOf(Login login) {
        return users.ownerOf(login);
    }

    @Override
    public boolean has(EmailAddress email) { return users.has(email); }

    @Override
    public List<User> list() { return users.list(); }
}
