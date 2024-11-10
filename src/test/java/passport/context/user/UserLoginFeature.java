package passport.context.user;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;

import passport.domain.contexts.user.UserLogin;
import passport.domain.exceptions.PermissionDenied;
import passport.domain.models.users.Login;
import passport.infra.Session;
import passport.resources.bdd.Assume;
import passport.resources.bdd.Feature;
import passport.resources.bdd.Given;
import passport.resources.bdd.Scenario;
import passport.resources.bdd.Then;
import passport.resources.bdd.When;
import passport.resources.entities.ConcreteUsers;
import passport.roles.repositories.Users;

@Feature("User Login")
public class UserLoginFeature {

    private final Users users = ConcreteUsers.withJohnAndJane();

    @Scenario("Logging in as an existing user")
    @Given("An anonymous user")
    @Assume("Session is inactive")
    @When("Logging in as an existing user")
    @Then("Should log in as this user")
    @Test
    void shouldLogInAsUser() {
        var session = new Session();
        assumeFalse(session.isActive());

        assertDoesNotThrow(() -> new UserLogin(users)
                .withSession(session)
                .logAs(Login.of("john.doe@example.com", "123456")));

        assertEquals(ConcreteUsers.JohnDoe(), session.loggedUser().get());
        assertTrue(session.isActive());
    }

    @Scenario("Changing login to another existing user")
    @Given("A logged-in session")
    @Assume("Session is active")
    @When("Logging in as another existing user")
    @Then("Should change the current user to the new one")
    @Test
    void shouldLogInAsAnotherUser() {
        var session = Session.loggedAs(ConcreteUsers.JohnDoe());
        assumeTrue(session.isActive());
        assumeTrue(session.loggedUser().get().equals(ConcreteUsers.JohnDoe()));

        assertDoesNotThrow(() -> new UserLogin(users)
                .withSession(session)
                .logAs(Login.of("jane.doe@example.com", "789123")));

        assertEquals(ConcreteUsers.JaneDoe(), session.loggedUser().get());
        assertTrue(session.isActive());
    }

    @Scenario("Trying to log in as a nonexistent user")
    @Given("A logged-in session")
    @Assume("Session is active")
    @When("Trying to log in as a nonexistent user")
    @Then("Should throw, and keep the same session state")
    @Test
    void shouldThrow() {
        var session = Session.loggedAs(ConcreteUsers.JohnDoe());
        assumeTrue(session.isActive());
        assumeTrue(session.loggedUser().get().equals(ConcreteUsers.JohnDoe()));

        assertThrowsExactly(PermissionDenied.class,
                () -> new UserLogin(users)
                        .withSession(session)
                        .logAs(Login.of("johane.doe@example.com", "789123")));

        assertThrowsExactly(PermissionDenied.class, () -> new UserLogin(users)
                .withSession(session)
                .logAs(Login.of("jane.doe@example.com", "123456")));

        assertEquals(ConcreteUsers.JohnDoe(), session.loggedUser().get());
        assertTrue(session.isActive());
    }

    @Scenario("Logging out as an anonymous user")
    @Given("An anonymous user")
    @Assume("Session is inactive")
    @When("Trying to logout")
    @Then("Should remain anonymous")
    @Test
    void shouldRemainAnonymousWhenLoggingOut() {
        var session = new Session();
        assumeFalse(session.isActive());

        new UserLogin(users)
                .withSession(session)
                .logOut();

        assertTrue(session.loggedUser().isEmpty());
        assertFalse(session.isActive());
    }

    @Scenario("Logging out as a logged-in user")
    @Given("A logged-in session")
    @Assume("Session is active")
    @When("Logging out")
    @Then("Should disable the session")
    @Test
    void shouldDisableSessionWhenLoggingOut() {
        var session = Session.loggedAs(ConcreteUsers.JohnDoe());
        assumeTrue(session.isActive());
        assumeTrue(session.loggedUser().get().equals(ConcreteUsers.JohnDoe()));

        new UserLogin(users)
                .withSession(session)
                .logOut();

        assertTrue(session.loggedUser().isEmpty());
        assertFalse(session.isActive());
    }
}
