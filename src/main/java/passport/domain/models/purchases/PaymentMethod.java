package passport.domain.models.purchases;

import passport.domain.models.email.Html;

/**
 * Represents a payment method with details, and provides HTML representation
 * for display.
 */
public class PaymentMethod {
    private final String method;
    private final String details;

    /**
     * Constructs a new PaymentMethod with the specified method and details.
     *
     * @param method  the type of payment method
     * @param details the specific details of the payment method
     */
    public PaymentMethod(String method, String details) {
        this.method = method;
        this.details = details;
    }

    /**
     * Returns the HTML representation of the payment method.
     *
     * @return the HTML string representing the payment method
     */
    public String html() {
        return new StringBuilder(header())
                .append(details())
                .toString();
    }

    /**
     * Creates the HTML header for the payment method.
     *
     * @return the HTML header as a string
     */
    private String header() {
        var header = Html.node("h2")
                .content("Payment Method")
                .toString();

        return header;
    }

    /**
     * Creates the HTML details for the payment method.
     *
     * @return the HTML details as a string
     */
    private String details() {
        final var methodHtml = listItem("Method", method);
        final var detailsHtml = listItem("Details", details);

        return Html.node("ul")
                .content(methodHtml + detailsHtml)
                .toString();
    }

    /**
     * Creates an HTML list item with the specified title and description.
     *
     * @param title       the title of the list item
     * @param description the description of the list item
     * @return the HTML list item as a string
     */
    private String listItem(String title, String description) {
        return Html.node("li")
                .content(title + ": " + description)
                .toString();
    }
}
