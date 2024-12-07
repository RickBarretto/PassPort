package passport.application.desktop;

import java.time.LocalDate;
import java.util.List;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import passport.application.desktop.system.Infra;
import passport.application.desktop.system.PassPort;
import passport.application.desktop.system.Services;
import passport.application.desktop.ui.welcome.WelcomeWindow;
import passport.domain.contexts.events.AvailableEventsListing;
import passport.domain.contexts.events.EvaluationListing;
import passport.domain.contexts.events.EventEvaluation;
import passport.domain.contexts.purchases.TicketBuying;
import passport.domain.contexts.user.SigningUp;
import passport.domain.contexts.user.SubscribedEventsListing;
import passport.domain.contexts.user.UserEditing;
import passport.domain.contexts.user.UserLogin;
import passport.infra.DisabledEmailService;
import passport.infra.Session;
import passport.infra.json.EventsJson;
import passport.infra.json.JsonFile;
import passport.infra.json.UsersJson;
import passport.infra.placeholders.ColdEvents;
import passport.infra.placeholders.ColdUsers;
import passport.infra.virtual.EventsInMemory;
import passport.infra.virtual.UsersInMemory;

/**
 * Main class of the PassPort application that extends {@link Application}.
 * Initializes and configures the application from different startup modes.
 */
public class App extends Application {
    private PassPort self;

    /**
     * UI startup method. This method overrides the method from
     * {@link Application}. Configures the application style, initializes the
     * welcome window, and displays the interface.
     * 
     * @param root The main stage of the application.
     */
    @Override
    public void start(Stage root) {
        startupFromCLI();

        this.self = self.withStage(root);
        Application.setUserAgentStylesheet(
                new PrimerDark().getUserAgentStylesheet());

        WelcomeWindow welcomeWindow = new WelcomeWindow(self);
        Scene scene = new Scene(welcomeWindow, 1200, 700);

        setupRoot(root, scene);
        root.show();
    }

    /**
     * Method to initialize the application from command line arguments. Checks
     * arguments and adjusts the startup mode accordingly.
     */
    private void startupFromCLI() {
        List<String> args = this.getParameters().getRaw();

        if (args.size() > 0) {
            switch (args.get(0)) {
            case "--dry" -> this.setDryrunStartup();
            case "--cold" -> this.setColdStartup();
            default -> this.help();
            }
        }
        else {
            this.setDefaultStartup();
        }
    }

    /**
     * Configures the main stage of the application with title and scene.
     * 
     * @param root  The main stage of the application.
     * @param scene The scene to be displayed on the main stage.
     */
    private void setupRoot(Stage root, Scene scene) {
        root.setTitle("PassPort");
        root.setScene(scene);
        self.toScene(scene);
    }

    /**
     * Main method that starts the application.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) { launch(args); }

    /**
     * Displays the help message with the supported command line arguments.
     */
    private void help() { System.out.println("""
            PassPort

            Arguments:
                --dry-run   Run with a virtual database.
                            So, nothing will be stored.
                --cold      Run with default cold Database.
                            All custom data, will be removed.
                --help      Show this
            """); }

    /**
     * Initializes the application in dry-run mode. In this mode, nothing will
     * be stored. Data will not be used from the database, but instead, default
     * data will be loaded into RAM only.
     */
    private void setDryrunStartup() {
        System.out.println("Starting up with Dry-run mode...");
        this.self = new PassPort(servicesOf(new Infra(
                new UsersInMemory(ColdUsers.list),
                new EventsInMemory(ColdEvents.list),
                new DisabledEmailService(),
                new Session())));
    }

    /**
     * Initializes the application in cold-startup mode. In this mode, custom
     * data will be overwritten by default data, the database will be used, and
     * data modified during application use will be updated and saved to the
     * database.
     */
    private void setColdStartup() {
        System.out.println("Starting up with Cold mode...");

        var coldUsers = new UsersInMemory(ColdUsers.list);
        var coldEvents = new EventsInMemory(ColdEvents.list);
        var usersJson = new JsonFile("data", "users");
        var eventsJson = new JsonFile("data", "events");

        this.self = new PassPort(servicesOf(
                new Infra(
                        new UsersJson(usersJson, coldUsers),
                        new EventsJson(eventsJson, coldEvents),
                        new DisabledEmailService(),
                        new Session())));
    }

    /**
     * Initializes the application in default mode. The default mode uses the
     * database present in the data/ folder. Nothing is overwritten during
     * application startup.
     */
    private void setDefaultStartup() {
        System.out.println("Starting up with Default mode...");
        this.self = new PassPort(servicesOf(
                new Infra(
                        new UsersJson(new JsonFile("data", "users")),
                        new EventsJson(new JsonFile("data", "events")),
                        new DisabledEmailService(),
                        new Session())));
    }

    /**
     * Configures and returns the application's services according to a
     * customized infrastructure.
     * 
     * @param infra The infrastructure used to configure the services.
     * @return An instance of {@link Services} with all the services configured.
     */
    private Services servicesOf(Infra infra) {
        return new Services(
                new SigningUp(infra.users()),
                new UserLogin(infra.session(), infra.users()),
                new AvailableEventsListing(infra.events()),
                new SubscribedEventsListing(infra.events()),
                new EvaluationListing(infra.users()),
                new EventEvaluation(infra.events(), LocalDate.now()),
                new TicketBuying(infra.events(), infra.users(),
                        LocalDate.now()),
                new UserEditing(infra.users()));
    }
}
