package test.roles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import main.roles.Entity;
import main.roles.EntityId;

class SomeId extends EntityId {
    public SomeId(UUID id) { super(id); }

    public SomeId() { super(); }
}

class SomeEntity extends Entity<SomeId> {
    public SomeEntity(SomeId id) { super(id); }
}

public class EntityTest {

    @Nested
    class IdImplTest {
        @Test
        void testEquals() {
            var id = "78266c37-e43b-48c5-9a4b-0c996d431d02";
            var actual = new SomeId(UUID.fromString(id));

            assertTrue(actual.equals(actual));
            assertTrue(actual.equals(new SomeId(UUID.fromString(id))));
            assertTrue(actual.equals(new SomeId(actual.value())));

            var otherId = "78266c38-e43b-48c5-9a4b-0c996d431d02";
            assertFalse(actual.equals(new SomeId(UUID.fromString(otherId))));
            assertFalse(actual.equals(new Object()));
        }
    }

    @Nested
    class EntityImplTest {
        private SomeId id;
        private SomeId otherId;
        private SomeEntity actual;

        @BeforeEach
        void init() {
            id = new SomeId(
                    UUID.fromString("78266c37-e43b-48c5-9a4b-0c996d431d02"));
            otherId = new SomeId(
                    UUID.fromString("78266c38-e43b-48c5-9a4b-0c996d431d02"));
            actual = new SomeEntity(id);
        }

        @Test
        void testEquals() {
            assertTrue(actual.equals(actual));
            assertTrue(actual.equals((Object) actual));
            assertTrue(actual.equals(new SomeEntity(id)));

            assertFalse(actual.equals(new SomeEntity(otherId)));
            assertFalse(actual.equals(new Object()));
        }

        @Test
        void testHashCode() {
            assertEquals(actual.hashCode(), new SomeEntity(id).hashCode());
            assertNotEquals(actual.hashCode(), new SomeEntity(otherId).hashCode());
        }
    }

}
