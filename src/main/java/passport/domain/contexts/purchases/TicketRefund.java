package passport.domain.contexts.purchases;

import java.time.LocalDate;
import java.util.Objects;

import passport.domain.contexts.purchases.internal.Purchase;
import passport.domain.contexts.purchases.internal.PurchaseMail;
import passport.domain.exceptions.InvalidRefundDueToInactiveEvent;
import passport.domain.models.events.Ticket;
import passport.domain.models.purchases.PaymentMethod;
import passport.domain.models.users.UserId;
import passport.infra.DisabledEmailService;
import passport.roles.Context;
import passport.roles.EmailService;
import passport.roles.repositories.Events;
import passport.roles.repositories.Users;

/**
 * Context for processing ticket refunds.
 */
public class TicketRefund implements Context {
    private final LocalDate currentDay;
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
    public TicketRefund(Events events, Users users, LocalDate currentDay) {
        this(new DisabledEmailService(), events, users, currentDay);
    }

    /**
     * Constructor with specified email service.
     *
     * @param service the email service
     * @param events  the events repository
     * @param users   the users repository
     */
    public TicketRefund(EmailService service, Events events, Users users, LocalDate currentDay) {
        this.events = events;
        this.users = users;
        this.service = service;
        this.currentDay = currentDay;
    }

    /**
     * Sets the customer who will receive the refund.
     *
     * @param customerId the user ID of the customer
     * @return the updated TicketRefund object
     */
    public TicketRefund to(UserId customerId) {
        purchase.buyer = users.byId(customerId).get();
        return this;
    }

    /**
     * Sets the ticket to be refunded.
     *
     * @param ticket the ticket to be refunded
     * @return the updated TicketRefund object
     */
    public TicketRefund owning(Ticket ticket) throws InvalidRefundDueToInactiveEvent {
        purchase.event = this.events.byId(ticket.event()).get();
        
        if (!purchase.event.isAvailableFor(currentDay))
            throw new InvalidRefundDueToInactiveEvent();

        purchase.ticket = ticket;
        return this;
    }

    /**
     * Sets the payment method for the refund.
     *
     * @param paymentMethod the payment method
     * @return the updated TicketRefund object
     */
    public TicketRefund via(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    /**
     * Executes the refund process.
     */
    public void refund() {
        purchase.refund();
        saveRefunding();
        sendEmail();
    }

    /**
     * Sends a refund confirmation email.
     */
    private void sendEmail() {
        Objects.requireNonNull(paymentMethod);
        
        var emailDoc = new PurchaseMail()
            .of(purchase)
            .via(paymentMethod)
            .refundMail();
            
        service.send(emailDoc);
    }

    /**
     * Saves the refunding process to the repositories.
     */
    private void saveRefunding() {
        events.update(purchase.event);
        users.update(purchase.buyer, purchase.buyer);
    }
}
