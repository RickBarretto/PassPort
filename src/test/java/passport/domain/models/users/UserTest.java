package passport.domain.models.users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import passport.domain.models.users.values.EmailAddress;
import passport.domain.models.users.values.Password;

public class UserTest {
    private User actual;

    @BeforeEach
    void init() {
        var id = new UserId(
                UUID.fromString("78266c37-e43b-48c5-9a4b-0c996d431d02"));
        var login = new Login(new EmailAddress("john.doe@example.com"),
                new Password("123456"));
        var person = new Person("John Doe", "000.000.000-00");
        actual = new User(id, login, person);
    }

    @Nested
    class GettersTest {
        @Test
        void testIsAdmin() {
            assertFalse(actual.isAdmin());
            assertTrue(actual.asAdmin().isAdmin());
        }

        @Test
        void testLogin() {
            var expected = new Login(new EmailAddress("john.doe@example.com"),
                    new Password("123456"));
            assertEquals(expected, actual.login());

            var expectedToBeFalse = new Login(
                    new EmailAddress("jane.doe@example.com"),
                    new Password("123456"));
            assertNotEquals(expectedToBeFalse, actual.login());
        }

        @Test
        void testPerson() {
            var expected = new Person("John Doe", "000.000.000-00");
            assertEquals(expected, actual.person());

            var expectedToBeFalse = new Person("Jane Doe", "000.000.000-00");
            assertNotEquals(expectedToBeFalse, actual.person());
        }
    }

    @Nested
    class FacotryMethodTests {

        @Test
        void testAsAdmin() {
            assertFalse(actual.equals(actual.asAdmin()));
            assertNotEquals(actual, actual.asAdmin());
            assertInstanceOf(User.class, actual.asAdmin());
            assertTrue(actual.asAdmin().isAdmin());
        }

        @Test
        void testWithLogin() {
            var newLogin = new Login(new EmailAddress("jane.doe@example.com"),
                    new Password("123456"));
            assertNotEquals(actual, actual.with(newLogin));
            assertInstanceOf(User.class, actual.with(newLogin));
            assertEquals(newLogin, actual.with(newLogin).login());
        }

        @Test
        void testWithPerson() {
            var newPerson = new Person("Jane Doe",
                    "123456");
            assertNotEquals(actual, actual.with(newPerson));
            assertInstanceOf(User.class, actual.with(newPerson));
            assertEquals(newPerson, actual.with(newPerson).person());
        }
    }

    @Nested
    class ObjectTests {
        @Test
        void testEquals() {
            assertEquals(actual, actual);
            var otherId = new UserId(
                    UUID.fromString("78266c38-e43b-48c5-9a4b-0c996d431d02"));
            assertNotEquals(actual,
                    new User(otherId, actual.login(), actual.person()));
            assertNotEquals(actual, new Object());
        }

        @Test
        void testHashCode() {
            assertEquals(actual.hashCode(), actual.hashCode());
            assertNotEquals(actual
                    .with(new Person("Jane Doe", "000.000.000-00")).hashCode(),
                    actual.hashCode());
            assertNotEquals(actual
                    .with(new Login(new EmailAddress("jane.doe@example.com"),
                            new Password("123456")))
                    .hashCode(), actual.hashCode());
            assertNotEquals(actual.asAdmin().hashCode(), actual.hashCode());
        }
    }

}
