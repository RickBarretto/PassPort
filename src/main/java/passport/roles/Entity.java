package passport.roles;

/**
 * Represents a generic entity within the domain. This interface defines a
 * contract for entities to provide their unique identifier.
 *
 * @param <Id> the type of the entity's identifier
 */
public interface Entity<Id extends EntityId> {

    /**
     * Returns the unique identifier of the entity.
     *
     * @return the entity's unique identifier
     */
    public Id id();
}
