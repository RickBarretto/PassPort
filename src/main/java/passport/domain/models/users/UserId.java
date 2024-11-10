package passport.domain.models.users;

import java.util.UUID;

import passport.roles.EntityId;

public class UserId extends EntityId {
    public UserId(UUID id) { super(id); }

    public UserId() { super(); }
}
