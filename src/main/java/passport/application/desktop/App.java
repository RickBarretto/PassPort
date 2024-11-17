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
import passport.domain.contexts.events.EventEvaluation;
import passport.domain.contexts.purchases.TicketBuying;
import passport.domain.contexts.user.SigningUp;
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

public class App extends Application {
    private PassPort self;

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

    private void setupRoot(Stage root, Scene scene) {
        root.setTitle("PassPort");
        root.setScene(scene);
        self.toScene(scene);
    }

    public static void main(String[] args) { launch(args); }

    private void help() { System.out.println("""
            PassPort

            Arguments:
                --dry-run   Run with a virtual database.
                            So, nothing will be stored.
                --cold      Run with default cold Database.
                            All custom data, will be removed.
                --help      Show this
            """); }

    private void setDryrunStartup() {
        System.out.println("Starting up with Dry-run mode...");
        this.self = new PassPort(servicesOf(new Infra(
                new UsersInMemory(ColdUsers.list),
                new EventsInMemory(ColdEvents.list),
                new DisabledEmailService(),
                new Session())));
    }

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

    private void setDefaultStartup() {
        System.out.println("Starting up with Default mode...");
        this.self = new PassPort(servicesOf(
                new Infra(
                        new UsersJson(new JsonFile("data", "users")),
                        new EventsJson(new JsonFile("data", "events")),
                        new DisabledEmailService(),
                        new Session())));
    }

    private Services servicesOf(Infra infra) {
        return new Services(
                new SigningUp(infra.users()),
                new UserLogin(infra.session(), infra.users()),
                new AvailableEventsListing(infra.events()),
                new EventEvaluation(infra.events(), LocalDate.now()),
                new TicketBuying(infra.events(), infra.users(), LocalDate.now()),
                new UserEditing(infra.users()));
    }
}