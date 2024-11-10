package passport.context.purchase;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import passport.domain.contexts.purchases.internal.Purchase;
import passport.domain.contexts.purchases.internal.PurchaseMail;
import passport.domain.models.email.EmailDocument;
import passport.domain.models.events.Event;
import passport.domain.models.events.Poster;
import passport.domain.models.events.Ticket;
import passport.domain.models.purchases.PaymentMethod;
import passport.resources.bdd.Feature;
import passport.resources.bdd.Given;
import passport.resources.bdd.Then;
import passport.resources.bdd.When;
import passport.resources.entities.ConcreteUsers;

@Feature("Mailing Buyer")
public class MailingBuyerFeature {

    private final PaymentMethod method = new PaymentMethod("PIX",
            "Chave aleatória: d4f8e20b-48fa-4c91-9363-e067c074fa75");
    private final Event event = new Event(
            new Poster("From Zero", "A LP show", LocalDate.of(2024, 11, 15)),
            850.00);
    private final Ticket ticket = new Ticket(event.id(), 850.00).packedFor(3);

    private PurchaseMail service;

    @BeforeEach
    void setUp() {
        var purchase = new Purchase();
        purchase.buyer = ConcreteUsers.JohnDoe();
        purchase.ticket = ticket;
        purchase.event = event;

        service = new PurchaseMail()
                .of(purchase)
                .via(method);
    }

    @Given("A buyer has purchased tickets")
    @When("A purchase email is generated")
    @Then("The email should contain purchase details")
    @Test
    void testPurchase() {
        final EmailDocument email = service.purchaseMail();
        final String content = email.body();

        assertTrue(content.contains("A 3-person ticket"));
        assertTrue(content.contains(" was purchased for From Zero"));
        assertFalse(content.contains("Refunding in"));
    }

    @Given("A buyer has purchased tickets")
    @When("A refund email is generated")
    @Then("The email should contain refund details")
    @Test
    void testRefundMessage() {
        final EmailDocument email = service.refundMail();
        final String content = email.body();

        assertTrue(content.contains("Refunding in R$2550.00"));
        assertTrue(content.contains(" for From Zero purchase."));
        assertFalse(content.contains("was purchased"));
    }
}
