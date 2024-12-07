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
 * Classe principal da aplicação PassPort que estende {@link Application}.
 * Inicializa e configura a aplicação a partir de diferentes modos de
 * inicialização.
 */
public class App extends Application {
    private PassPort self;

    /**
     * Método de início da UI. O médoto sobreescreve o método
     * de{@link Application}. Configura o estilo da aplicação, inicializa a
     * janela de boas-vindas e exibe a interface.
     * 
     * @param root O estágio principal da aplicação.
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
     * Método para inicializar a aplicação a partir dos argumentos da linha de
     * comando. Verifica argumentos e ajusta o modo de inicialização
     * adequadamente.
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
     * Configura o estágio principal da aplicação com título e cena.
     * 
     * @param root  O estágio principal da aplicação.
     * @param scene A cena a ser exibida no estágio principal.
     */
    private void setupRoot(Stage root, Scene scene) {
        root.setTitle("PassPort");
        root.setScene(scene);
        self.toScene(scene);
    }

    /**
     * Método principal que inicia a aplicação.
     * 
     * @param args Argumentos da linha de comando.
     */
    public static void main(String[] args) { launch(args); }

    /**
     * Exibe a mensagem de ajuda com os argumentos suportados pela linha de
     * comando.
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
     * Inicializa a aplicação no modo de execução a seco (dry-run). Nesse modo,
     * nada será armazenado. Não será utilizado dados do banco de dados, mas
     * dados de povoamento (no sentido de default) que serão carregados na
     * memória RAM e somente nela.
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
     * Inicializa a aplicação no modo frio (cold-startup). Nesse modo, os dados
     * personalizados serão sobrescritos por dados de povoamento (no sentido de
     * default), é utilizado o banco de dados, e os dados modificados durante o
     * uso da aplicação serão atualizados e salvos no banco de dados.
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
     * Inicializa a aplicação no modo padrão. O modo padrão utiliza o bando de
     * dados presente na pasta data/. Nada é sobreescrito na inicialização da
     * aplicação.
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
     * Configura e retorna os serviços da aplicação, de acordo com uma
     * infraestrutura personalizada.
     * 
     * @param infra A infraestrutura usada para configurar os serviços.
     * @return Uma instância de {@link Services} com todos os serviços
     *         configurados.
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
