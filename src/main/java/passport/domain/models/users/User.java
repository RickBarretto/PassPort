package passport.domain.models.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import passport.domain.models.events.Ticket;
import passport.roles.Entity;

/**
 * Represents a user, including login details, personal details, and purchased
 * tickets.
 */
public class User implements Entity<UserId> {
    private final UserId id;
    private final Login login;
    private final Person person;
    private final boolean admin;
    private ArrayList<Ticket> boughtTickets;

    /**
     * Constructor for creating a new user with login and personal details.
     *
     * @param login  the login details
     * @param person the personal details
     */
    public User(Login login, Person person) {
        this(new UserId(), login, person);
    }

    /**
     * Constructor for creating a new user with a specified ID, login, and
     * personal details.
     *
     * @param id     the user ID
     * @param login  the login details
     * @param person the personal details
     */
    public User(UserId id, Login login, Person person) {
        this(id, login, person, false, new ArrayList<>());
    }

    /**
     * Private constructor for creating a new user with all details.
     *
     * @param id      the user ID
     * @param login   the login details
     * @param person  the personal details
     * @param admin   whether the user is an admin
     * @param tickets the list of bought tickets
     */
    private User(UserId id, Login login, Person person, boolean admin,
            ArrayList<Ticket> tickets) {
        this.id = id;
        this.login = login;
        this.person = person;
        this.admin = admin;
        this.boughtTickets = tickets;
    }

    /**
     * Sets the tickets for the user.
     *
     * @param tickets the list of tickets
     * @return a new User object with the specified tickets
     */
    public User withTickets(List<Ticket> tickets) {
        return new User(id, login, person, admin, new ArrayList<>(tickets));
    }

    /**
     * Checks if the user owns the specified login.
     *
     * @param login the login to check
     * @return true if the user owns the login, false otherwise
     */
    public boolean isOwnerOf(Login login) {
        return this.login.equals(login);
    }

    /**
     * Buys a ticket and adds it to the user's bought tickets.
     *
     * @param ticket the ticket to buy
     */
    public void buyTicket(Ticket ticket) { this.boughtTickets.add(ticket); }

    /**
     * Returns a ticket and removes it from the user's bought tickets.
     *
     * @param ticket the ticket to return
     */
    public void returnTicket(Ticket ticket) {
        this.boughtTickets.remove(ticket);
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public UserId id() { return id; }

    /**
     * Returns the login details.
     *
     * @return the login details
     */
    public Login login() { return login; }

    /**
     * Returns the personal details.
     *
     * @return the personal details
     */
    public Person person() { return person; }

    /**
     * Checks if the user is an admin.
     *
     * @return true if the user is an admin, false otherwise
     */
    public boolean isAdmin() { return admin; }

    /**
     * Returns the list of bought tickets.
     *
     * @return an unmodifiable list of bought tickets
     */
    public List<Ticket> tickets() { return List.copyOf(boughtTickets); }

    /**
     * Promotes the user to admin.
     *
     * @return a new User object with admin privileges
     */
    public User asAdmin() {
        return new User(id, login, person, true, boughtTickets);
    }

    /**
     * Sets the login details for the user.
     *
     * @param login the login details
     * @return a new User object with the specified login
     */
    public User with(Login login) {
        return new User(id, login, person, admin, boughtTickets);
    }

    /**
     * Sets the personal details for the user.
     *
     * @param person the personal details
     * @return a new User object with the specified personal details
     */
    public User with(Person person) {
        return new User(id, login, person, admin, boughtTickets);
    }

    public User copy() {
        return new User(id, login, person, admin, boughtTickets);
    }

    @Override
    public int hashCode() { return Objects.hash(id, login, person, admin); }

    public boolean equals(User other) {
        return Objects.equals(login, other.login)
                && Objects.equals(person, other.person)
                && Objects.equals(admin, other.admin)
                && Objects.equals(id, other.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User user)
            return this.equals(user);
        return false;
    }
}
