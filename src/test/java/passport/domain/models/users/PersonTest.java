package passport.domain.models.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PersonTest {
    Person person;

    @BeforeEach
    void init() { person = new Person("John Doe", "000.000.000-00"); }

    @Nested
    class TestGetters {

        @Test
        void testName() {
            assertEquals("John Doe", person.name());
            assertNotEquals("Jane Doe", person.name());
        }

        @Test
        void testCpf() {
            assertEquals("000.000.000-00", person.cpf());
            assertNotEquals("111.111.111-11", person.cpf());
        }
    }

    @Nested
    class TestObject {

        @Test
        void testEquals() {
            assertTrue(person.equals(new Person("John Doe", "000.000.000-00")));

            assertFalse(person.equals(new Object()));
            assertFalse(
                    person.equals(new Person("Jane Doe", "000.000.000-00")));
            assertFalse(
                    person.equals(new Person("John Doe", "111.111.111-11")));
        }

        @Test
        void testHashCode() {
            assertEquals(new Person("John Doe", "000.000.000-00").hashCode(),
                    person.hashCode());
            assertNotEquals(new Person("Joh Doe", "000.000.000-00").hashCode(),
                    person.hashCode());
            assertNotEquals(new Person("John Doe", "000.000.000-01").hashCode(),
                    person.hashCode());
        }
    }

    @Nested
    class TestFactoryMethods {

        @Test
        void testWithName() {
            var expected = new Person("Jane Doe", "000.000.000-00");
            assertNotEquals(person, person.withName("Jane Doe"));
            assertEquals(expected, person.withName("Jane Doe"));
        }

        @Test
        void testWithCpf() {
            var expected = new Person("John Doe", "000.000.000-01");
            assertNotEquals(person, person.withCpf("000.000.000-01"));
            assertEquals(expected, person.withCpf("000.000.000-01"));
        }
    }
}
