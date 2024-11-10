package passport.domain.models.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import passport.domain.models.purchases.PaymentDetails;
import passport.domain.models.purchases.PaymentMethod;

public class PaymentDetailsTest {
    PaymentMethod method = new PaymentMethod("PIX",
            "Chave aleatória: d4f8e20b-48fa-4c91-9363-e067c074fa75");
    PaymentDetails details = new PaymentDetails(method, 10.50);

    @Test
    void testAmount() { assertEquals("R$10.50", details.amount()); }

    @Test
    void testHtml() {
        var content = details.html();
        assertTrue(content.contains("<h2>Payment Method</h2>"));
        assertTrue(content.contains("<li>Method: PIX</li>"));
        assertTrue(content.contains(
                "<li>Details: Chave aleatória: d4f8e20b-48fa-4c91-9363-e067c074fa75</li>"));
    }
}
