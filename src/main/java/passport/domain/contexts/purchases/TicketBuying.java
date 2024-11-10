package passport.domain.contexts.purchases;

import java.time.LocalDate;
import java.util.Objects;

import passport.domain.contexts.purchases.internal.Purchase;
import passport.domain.contexts.purchases.internal.PurchaseMail;
import passport.domain.exceptions.PurchaseForInactiveEvent;
import passport.domain.exceptions.SoldOut;
import passport.domain.models.events.EventId;
import passport.domain.models.purchases.PaymentMethod;
import passport.domain.models.users.UserId;
import passport.infra.DisabledEmailService;
import passport.roles.Context;
import passport.roles.EmailService;
import passport.roles.repositories.Events;
import passport.roles.repositories.Users;

/**
 * Context for buying tickets for an event.
 */
public class TicketBuying implements Context {
    private final LocalDate currentDate;
    private final EmailService service;

    private Purchase purchase = new Purchase();
    private PaymentMethod paymentMethod;
    private Events events;
    private Users users;

    /**
     * Constructor with default email service.
     *
     * @param events the events repository
     * @param users  the users repository
     */
    public TicketBuying(Events events, Users users, LocalDate currentDate) {
        this(new DisabledEmailService(), events, users, currentDate);
    }

    /**
     * Constructor with specified email service.
     *
     * @param service     the email service
     * @param events      the events repository
     * @param users       the users repository
     * @param currentDate the current date of the context
     */
    public TicketBuying(EmailService service, Events events, Users users, LocalDate currentDate) {
        this.events = events;
        this.users = users;
        this.service = service;
        this.currentDate = currentDate;
    }

    /**
     * Sets the event for which tickets are being bought.
     *
     * @param event the event ID
     * @return the updated TicketBuying object
     */
    public TicketBuying of(EventId event) throws PurchaseForInactiveEvent {
        purchase.event = this.events.byId(event).get();

        if (!purchase.event.isAvailableFor(currentDate))
            throw new PurchaseForInactiveEvent();

        return this;
    }

    /**
     * Sets the customer who is buying the tickets.
     *
     * @param customer the user ID of the customer
     * @return the updated TicketBuying object
     */
    public TicketBuying by(UserId customer) {
        purchase.buyer = this.users.byId(customer).get();
        return this;
    }

    /**
     * Sets the payment method for the purchase.
     *
     * @param paymentMethod the payment method
     * @return the updated TicketBuying object
     */
    public TicketBuying via(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    /**
     * Executes the ticket purchase.
     *
     * @param amount the amount of tickets to buy
     * @throws SoldOut if the event is sold out
     */
    public void buy(Integer amount) throws SoldOut {
        purchase.makeOf(amount);
        savePurchase();
        sendEmail();
    }

    /**
     * Saves the purchase to the repositories.
     */
    private void savePurchase() {
        events.update(purchase.event);
        users.update(purchase.buyer, purchase.buyer);
    }

    /**
     * Sends a confirmation email for the purchase.
     */
    private void sendEmail() {
        Objects.requireNonNull(paymentMethod);

        var emailDoc = new PurchaseMail()
            .of(purchase)
            .via(paymentMethod)
            .purchaseMail();

        service.send(emailDoc);
    }
}
