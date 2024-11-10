package passport.domain.models.users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import passport.domain.models.users.values.EmailAddress;
import passport.domain.models.users.values.Password;

public class LoginTest {
    Login login;

    @BeforeEach
    void init() {
        login = new Login(new EmailAddress("john.doe@example.com"), 
                new Password("123456"));
    }

    @Nested
    class TestGetters {

        @Test
        void testEmail() {
            assertEquals(new EmailAddress("john.doe@example.com"),
                    login.email());
            assertNotEquals(new EmailAddress("jane.doe@example.com"),
                    login.email());

        }
    }

    @Nested
    class TestObject {

        @Test
        void testEquals() {
            assertTrue(
                    login.equals(
                            new Login(new EmailAddress("john.doe@example.com"),
                                    new Password("123456"))));

            assertFalse(login.equals(new Object()));
            assertFalse(
                    login.equals(
                            new Login(new EmailAddress("jane.doe@example.com"),
                                    new Password("123456"))));
            assertFalse(
                    login.equals(
                            new Login(new EmailAddress("john.doe@example.com"),
                                    new Password("789123"))));
        }

        @Test
        void testHashCode() {
            assertEquals(
                    new Login(new EmailAddress("john.doe@example.com"),
                            new Password("123456")).hashCode(),
                    login.hashCode());
            assertNotEquals(
                    new Login(new EmailAddress("john.do@example.com"), new Password("123456"))
                            .hashCode(),
                    login.hashCode());
        }
    }

    @Nested
    class TestFactoryMethods {

        @Test
        void testWithEmail() {
            var expected = new Login(new EmailAddress("jane.doe@example.com"),
                    new Password("123456"));
            assertNotEquals(login,
                    login.with(new EmailAddress("jane.doe@example.com")));
            assertEquals(expected,
                    login.with(new EmailAddress("jane.doe@example.com")));
        }

        @Test
        void testWithPassword() {
            var expected = new Login(new EmailAddress("john.doe@example.com"),
                    new Password("789123"));
            assertNotEquals(login, login.with(new Password("789123")));
            assertEquals(expected, login.with(new Password("789123")));
        }
    }

}
