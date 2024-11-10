package passport.domain.models.purchases;

import passport.domain.models.email.EmailDocument;
import passport.domain.models.email.EmailMetadata;
import passport.domain.models.email.Html;
import passport.roles.Entity;

/**
 * Represents a transaction including payer, recipient, payment details, and
 * description.
 */
public class Transaction implements Entity<TransactionId> {
    private final TransactionId id;
    private final String description;
    private final Participant payer;
    private final Participant recipient;
    private final PaymentDetails payment;

    /**
     * Constructs a new Transaction with the specified ID, payer, recipient,
     * payment details, and description.
     *
     * @param id          the transaction ID
     * @param payer       the participant making the payment
     * @param recipient   the participant receiving the payment
     * @param payment     the payment details
     * @param description the description of the transaction
     */
    public Transaction(TransactionId id, Participant payer,
            Participant recipient, PaymentDetails payment, String description) {
        this.id = id;
        this.payer = payer;
        this.recipient = recipient;
        this.payment = payment;
        this.description = description;
    }

    /**
     * Constructs a new Transaction with the specified payer, recipient, payment
     * details, and description.
     *
     * @param payer       the participant making the payment
     * @param recipient   the participant receiving the payment
     * @param payment     the payment details
     * @param description the description of the transaction
     */
    public Transaction(Participant payer, Participant recipient,
            PaymentDetails payment, String description) {
        this(new TransactionId(), payer, recipient, payment, description);
    }

    /**
     * Returns the transaction ID.
     *
     * @return the transaction ID
     */
    public TransactionId id() { return id; }

    /**
     * Converts the transaction details to an email document.
     *
     * @return the email document representing the transaction
     */
    public EmailDocument toEmail() {
        return new EmailDocument(
                new EmailMetadata(payer.email(), recipient.email(),
                        description),
                this.html());

    }

    /**
     * Generates the HTML content for the transaction.
     *
     * @return the HTML content as a string
     */
    private Html html() {
        var content = new StringBuilder("")
                .append(Html.node("h1").content(description))
                .append(transactionSect())
                .append(Html.br())
                .append(payment.html())
                .toString();

        return Html.node("body").content(content);
    }

    /**
     * Generates the transaction section of the HTML content.
     *
     * @return the transaction section as a string
     */
    private String transactionSect() {
        return new StringBuilder("Transaction by ")
                .append(payer)
                .append(" to ")
                .append(recipient)
                .append(" of ")
                .append(payment.amount())
                .append(".")
                .toString();
    }
}
