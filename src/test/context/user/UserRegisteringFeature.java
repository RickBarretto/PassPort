package test.context.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.domain.contexts.user.UserRegistering;
import main.domain.contexts.user.forms.PersonalInformation;
import main.domain.contexts.user.forms.LoginInformation;
import main.domain.exceptions.EmailAlreadyExists;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.values.EmailAddress;
import main.domain.models.users.values.Password;
import main.infra.virtual.UsersInMemory;
import main.roles.repositories.Users;
import test.resources.bdd.*;

@Feature("Registering a new user")
public class UserRegisteringFeature {

    Users repository;

    @BeforeEach
    void emptyRepository() { repository = new UsersInMemory(); }

    Login validLogin() {
        return new LoginInformation()
                .email("john.doe@example.com")
                .password("123456")
                .submit();
    }

    Person validPerson() {
        return new PersonalInformation()
                .name("John Doe")
                .cpf("000.000.000-00")
                .submit();
    }

    @Scenario("Successfully registering a user")
    @Given("Some Login and Some Person")
    @When("Registering Login and Person into a Repository")
    @Then("Should register if Email is available")
    @Test
    void shouldRegister() {
        // Precondition
        assumeFalse("Email must not be registered",
                repository.has(new EmailAddress("john.doe@example.com")));
        // Do
        assertDoesNotThrow(() -> {
            var login = validLogin();
            var person = validPerson();

            new UserRegistering(repository)
                    .login(login)
                    .person(person)
                    .register();
        });
        // Assertions
        final var login = Login.of("john.doe@example.com", "123456");
        var owner = repository.ownerOf(login);
        assertTrue("Email is now registered",
                repository.has(new EmailAddress("john.doe@example.com")));
        assertTrue("Owner is present", owner.isPresent());
    }

    @Scenario("Successfully registering a user")
    @Given("Some Login and Some Person")
    @When("Registering Login and Person into a Repository")
    @Then("Registered User is the same")
    @And("Should register if Email is available")
    @Test
    void shouldBeTheSame() {
        // Do
        assertDoesNotThrow(() -> {
            var login = validLogin();
            var person = validPerson();

            new UserRegistering(repository)
                    .login(login)
                    .person(person)
                    .register();
        });
        // Assertions
        final var login = Login.of("john.doe@example.com", "123456");
        var owner = repository
                .ownerOf(login)
                .get();
        assertEquals(new EmailAddress("john.doe@example.com"),
                owner.login().email());
        assertEquals("John Doe", owner.person().name());
        assertEquals("000.000.000-00", owner.person().cpf());
        assertFalse(owner.isAdmin());
    }

    @Scenario("Cannot register a user with an existing email")
    @Given("Some Login and Some Person")
    @When("Registering Login and Person twice into a Repository")
    @Then("Should throw EmailAlreadyExists if email is not available")
    @Test
    void shouldNotRegister() {
        // Do
        assertThrows(EmailAlreadyExists.class, () -> {
            var login = validLogin();
            var person = validPerson();

            var context = new UserRegistering(repository)
                    .login(login)
                    .person(person);

            context.register();
            context.register();
        });

        // Assertions
        assertTrue(repository.has(
                new EmailAddress("john.doe@example.com")));
    }
}
