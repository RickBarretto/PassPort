package passport.domain.models.purchases;

import java.util.UUID;

import passport.roles.EntityId;

public class TransactionId extends EntityId {
    public TransactionId(UUID id) { super(id); }

    public TransactionId() { super(); }
}
