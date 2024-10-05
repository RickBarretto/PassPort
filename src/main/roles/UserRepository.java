package main.roles;

import java.util.Optional;

import main.domain.models.users.User;
import main.domain.models.users.UserId;

public interface UserRepository {
    void register(User user);

    Optional<User> ownerOf(String email, String password);

    boolean has(UserId id);

    boolean has(String email);
}