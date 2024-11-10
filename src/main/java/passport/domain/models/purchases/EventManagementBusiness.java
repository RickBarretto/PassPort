package passport.domain.models.purchases;

import passport.domain.models.users.values.EmailAddress;

/**
 * Represents constant information for the Event Management Business, including
 * email, CNPJ, and company name.
 */
public class EventManagementBusiness {
    private final static EmailAddress email = new EmailAddress(
            "noreply@event.management");
    private final static String cnpj = "XX.XXX.XXX/0001-XX";
    private final static String company = "Event Management Startup";

    /**
     * Returns the email of the Event Management Business.
     *
     * @return the email address
     */
    public EmailAddress email() { return email; }

    /**
     * Returns the CNPJ of the Event Management Business.
     *
     * @return the CNPJ
     */
    public String cpnj() { return cnpj; }

    /**
     * Returns the company name of the Event Management Business.
     *
     * @return the company name
     */
    public String name() { return company; }
}
