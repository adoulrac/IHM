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
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		Application.launch(Main.class, (java.lang.String[]) null);
	}

    /**
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        main = new MainController(primaryStage);
    }

    private static MainController main;

    public static DATAtoIHM getDATAtoIHMimpl() {
        return main.getDATAInterfaceReceiver();
    }
}
