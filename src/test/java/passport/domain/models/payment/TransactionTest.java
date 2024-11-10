package passport.domain.models.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import passport.domain.models.purchases.Participant;
import passport.domain.models.purchases.PaymentDetails;
import passport.domain.models.purchases.PaymentMethod;
import passport.domain.models.purchases.Transaction;
import passport.domain.models.purchases.TransactionId;
import passport.domain.models.users.Login;
import passport.domain.models.users.Person;
import passport.domain.models.users.User;
import passport.domain.models.users.values.EmailAddress;
import passport.domain.models.users.values.Password;

public class TransactionTest {
    private final TransactionId randomId = new TransactionId(UUID.randomUUID());
    private final PaymentDetails payment = new PaymentDetails(
            new PaymentMethod("...", "..."),
            50.10);
    private final Transaction transaction = new Transaction(randomId,
            new Participant(
                    new User(
                            new Login(new EmailAddress("john.doe@example.com"),
                                    new Password("123456")),
                            new Person("John Doe", "000.000.000-00"))),
            new Participant(
                    new User(
                            new Login(new EmailAddress("jane.doe@example.com"),
                                    new Password("789123")),
                            new Person("Jane Doe", "111.111.111-11"))),
            payment, "Lending money to a friend");

    @Test
    void testId() {
        TransactionId expected = this.randomId;
        assertEquals(expected, transaction.id());
    }

    @Test
    void testSender() {
        final var expected = new EmailAddress("john.doe@example.com");
        final var actual = transaction.toEmail().metadata().sender();
        assertEquals(expected, actual);
    }

    @Test
    void testRecipient() {
        final var expected = new EmailAddress("jane.doe@example.com");
        final var actual = transaction.toEmail().metadata().recipient();
        assertEquals(expected, actual);
    }

    @Test
    void testEmailTitle() {
        final var expected = "Lending money to a friend";
        final var actual = transaction.toEmail().metadata().subject();
        assertEquals(expected, actual);
    }

    @Test
    void testHtml() {
        final var email = transaction.toEmail();
        var content = email.body().toString();
        assertTrue(content
                .contains("<h1>" + email.metadata().subject() + "</h1>"));
        assertTrue(
                content.contains("Transaction by John Doe (000.000.000-00)"));
        assertTrue(content.contains("to Jane Doe (111.111.111-11)"));
        assertTrue(content.contains("of R$50.10."));
        assertTrue(content.contains(payment.html()));
    }
}
