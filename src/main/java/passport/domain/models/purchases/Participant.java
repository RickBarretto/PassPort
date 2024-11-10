package passport.domain.models.purchases;

import passport.domain.models.users.User;
import passport.domain.models.users.values.EmailAddress;

/**
 * Represents a participant in a transaction, including email, name, and tax ID.
 * This class is designed to create generic Payment emails, accommodating both
 * user CPF and business CNPJ.
 */
public class Participant {
    private EmailAddress email;
    private String name;
    private String taxId;

    /**
     * Constructs a new Participant with the specified email, name, and tax ID.
     *
     * @param email the participant's email
     * @param name  the participant's name
     * @param taxId the participant's tax ID
     */
    private Participant(EmailAddress email, String name, String taxId) {
        this.email = email;
        this.name = name;
        this.taxId = taxId;
    }
    
    public Participant(String email, String name, String taxId) {
        this(new EmailAddress(email), name, taxId);
    }

    /**
     * Constructs a new Participant based on a user's information.
     *
     * @param customer the user whose information is used to create the
     *                     participant
     */
    public Participant(User customer) {
        this(customer.login().email(), customer.person().name(),
                customer.person().cpf());
    }

    /**
     * Creates a Participant representing the Event Management Business itself.
     *
     * @return a new Participant object representing the business
     */
    static public Participant self() {
        var business = new EventManagementBusiness();
        return new Participant(business.email(), business.name(),
                business.cpnj());
    }

    /**
     * Returns the email of the participant.
     *
     * @return the participant's email
     */
    public EmailAddress email() { return email; }

    @Override
    public String toString() {
        return new StringBuilder(name)
                .append(" (")
                .append(taxId)
                .append(")")
                .toString();
    }
}
