package passport.domain.models.purchases;

/**
 * Represents the details of a payment, including the payment method and amount.
 */
public class PaymentDetails {
    private final PaymentMethod method;
    private final Double amount;

    /**
     * Constructs a new PaymentDetails object with the specified payment method
     * and amount.
     *
     * @param method the payment method
     * @param amount the payment amount
     */
    public PaymentDetails(PaymentMethod method, Double amount) {
        this.method = method;
        this.amount = amount;
    }

    /**
     * Returns the formatted payment amount as a string.
     *
     * @return the formatted payment amount
     */
    public String amount() { return "R$" + String.format("%.2f", amount); }

    /**
     * Returns the HTML representation of the payment method.
     *
     * @return the HTML representation of the payment method
     */
    public String html() { return method.html(); }
}
