package IHM;

import IHM.controllers.MainController;
import IHM.interfaces.DATAtoIHM;
import javafx.application.Application;
import javafx.stage.Stage;

// TODO: Auto-generated Javadoc
/**
 * Main Application. This class handles navigation and user session.
 */
public class Main extends Application {

	/**
	 * The main method.
	 *
	 * @param args            the command line arguments
	 */
	public static void main(String[] args) {
		Application.launch(Main.class, (java.lang.String[]) null);
	}

    /**
     * Start.
     *
     * @param primaryStage the primary stage
     */
    @Override
    public void start(Stage primaryStage) {
        main = new MainController(primaryStage);
    }

    /** The main. */
    private static MainController main;

    /**
     * Gets the DAT ato ih mimpl.
     *
     * @return the DAT ato ih mimpl
     */
    public static DATAtoIHM getDATAtoIHMimpl() {
        return main.getDATAInterfaceReceiver();
    }
}
