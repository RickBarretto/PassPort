package test.contexts.customer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.core.contexts.customers.CustomerRegistering;
import main.core.contexts.customers.UserAlreadyRegistered;
import main.core.models.users.Customer;
import main.core.models.users.types.Account;
import main.core.models.users.types.CustomerID;
import main.core.models.users.types.Email;
import main.core.models.users.types.Username;
import main.infra.virtualdb.VirtualCustomerRepository;

/* Feature: Registering a new user
 *   In order to register new users
 *   As an Anonymous User
 *   I want to create a new Account
 * 
 */
public class RegistrationFeature 
{
    /* Scenario: Registering a new User
     *   Given An anonymous User
     *     And a repository 
     *     And an inexistent Account
     *   When creating a new Account
     *   Then A new account is created following the User's input
     */
    @Test
    @DisplayName("Given An anonymous User and an inexistent Account")
    void shouldRegister()
    {
        // Given an Account
        var newAccount = new Account(
            new Username("john.doe"),
            new Email("john@example.com"),
            "12345678"
            );
            
        // When registering a new Account
        var context = new CustomerRegistering(
            new VirtualCustomerRepository(),
            newAccount 
        );

        assertDoesNotThrow(() -> context.register());
        assertTrue(context.hasRegistered());
    }

    /* Scenario: Registering an existent User
    *   Given An anonymous User 
    *     And a registered Account
    *   When creating a new Account
    *   Then A new account can't be created
    *     And throws UserAlreadyRegistered
    */
    @Test
    @DisplayName("Given and Anonymous User and a registered Account")
    void shouldThrow()
    {
        // Given an empty repository
        var repository = new VirtualCustomerRepository();

        // Given a registered Customer
        var account = new Account(
            new Username("john.doe"),
            new Email("john@example.com"),
            "12345678"
        );

        repository.register(new Customer(new CustomerID(), account));

        // When creating a new account
        var context = new CustomerRegistering(repository, account);
        assertThrows(UserAlreadyRegistered.class, () -> context.register());
        assertFalse(context.hasRegistered());
    }
}
