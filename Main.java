package IHM;

import IHM.controllers.MainController;
import IHM.interfaces.DATAtoIHM;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main Application. This class handles navigation and user session.
 */
public class Main extends Application {

    /**
     * The main controller.
     */
    private static MainController mainController;

    /**
     * The main method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(Main.class, (java.lang.String[]) null);
    }

    /**
     * Gets the DAT ato ih mimpl.
     *
     * @return the DAT ato ih mimpl
     */
    public static DATAtoIHM getDATAtoIHMimpl() {
        return mainController.getDATAInterfaceReceiver();
    }

    /**
     * Start.
     *
     * @param primaryStage the primary stage
     */
    @Override
    public void start(final Stage primaryStage) {
        mainController = new MainController(primaryStage);
        if (mainController != null) {
            mainController.instanciateInterfaces();
        }
    }
}
