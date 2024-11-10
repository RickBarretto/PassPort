package passport.domain.contexts.purchases.internal;

import passport.domain.models.email.EmailDocument;
import passport.domain.models.purchases.Participant;
import passport.domain.models.purchases.PaymentDetails;
import passport.domain.models.purchases.PaymentMethod;
import passport.domain.models.purchases.Transaction;

/**
 * Manages the creation of purchase and refund emails.
 */
public class PurchaseMail {
    private final Participant self = Participant.self();
    private Purchase purchase;
    private PaymentMethod paymentMethod;

    /**
     * Sets the purchase details for the email.
     *
     * @param purchase the purchase details
     * @return the updated PurchaseMail object
     */
    public PurchaseMail of(Purchase purchase) {
        this.purchase = purchase;
        return this;
    }

    /**
     * Sets the payment method for the email.
     *
     * @param paymentMethod the payment method
     * @return the updated PurchaseMail object
     */
    public PurchaseMail via(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    /**
     * Creates an email document for the purchase.
     *
     * @return the email document for the purchase
     */
    public EmailDocument purchaseMail() {
        return mail(new Participant(purchase.buyer), self, purchaseMessage());
    }

    /**
     * Creates an email document for the refund.
     *
     * @return the email document for the refund
     */
    public EmailDocument refundMail() {
        return mail(self, new Participant(purchase.buyer), refundMessage());
    }

    /**
     * Generates an email document with the given payer, recipient, and
     * description.
     *
     * @param payer       the payer participant
     * @param recipient   the recipient participant
     * @param description the email description
     * @return the generated email document
     */
    private EmailDocument mail(Participant payer, Participant recipient,
            String description) {
        purchase.shouldBeSold();
        return new Transaction(payer, recipient, this.paymentDetails(),
                description).toEmail();
    }

    /**
     * Generates a ticket description.
     *
     * @return the ticket description
     */
    private String ticketDescription() {
        // E.g.: "2-person ticket"
        return new StringBuilder("").append(purchase.ticket.availableFor())
                .append("-person ticket").toString();
    }

    /**
     * Generates a purchase message.
     *
     * @return the purchase message
     */
    private String purchaseMessage() {
        var ticketDescription = ticketDescription();
        var eventDescription = purchase.event.poster().title();
        return new StringBuilder("A ").append(ticketDescription)
                .append(" was purchased for ").append(eventDescription)
                .toString();
    }

    /**
     * Generates a refund message.
     *
     * @return the refund message
     */
    private String refundMessage() {
        return new StringBuilder("Refunding in ")
                .append(String.format("R$%.2f", purchase.ticket.price()))
                .append(" for ").append(purchase.event.poster().title())
                .append(" purchase.").toString();
    }

    /**
     * Generates payment details for the transaction.
     *
     * @return the payment details
     */
    private PaymentDetails paymentDetails() {
        return new PaymentDetails(paymentMethod, purchase.ticket.price());
    }
}
