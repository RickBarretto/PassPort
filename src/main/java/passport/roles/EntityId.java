package passport.roles;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents the unique identifier for an entity within the domain. An EntityId
 * ensures that each entity has a unique identity that distinguishes it from all
 * other entities, crucial for lookup process in persistence. This class serves
 * as a base class for all entity identifiers, leveraging UUIDs for uniqueness.
 */
public abstract class EntityId {
    private UUID value;

    /**
     * Constructs a new EntityId with a randomly generated UUID.
     */
    public EntityId() { this.value = UUID.randomUUID(); }

    /**
     * Constructs a new EntityId with the specified UUID.
     *
     * @param value the UUID value for the entity ID
     */
    public EntityId(UUID value) { this.value = value; }

    /**
     * Returns the UUID value of the entity ID.
     *
     * @return the UUID value
     */
    public UUID value() { return value; }

    @Override
    public int hashCode() { return Objects.hash(value); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof EntityId))
            return false;
        var other = (EntityId) obj;
        return Objects.equals(value, other.value);
    }
}
