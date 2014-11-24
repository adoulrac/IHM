package IHM.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController extends Pane implements Initializable {

    private MainController application;

    @FXML
    private Button btnDeconnect;
    @FXML
    private Button btnGroupsHandling;
    @FXML
    private Button btnPreferences;
    @FXML
    private Button btnAddFriends;

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        // NOP
    }

    public void setApp(final MainController app) {
        this.application = app;
    }

    public void launchPreferences() {
        if(application != null) {
            application.goToProfile();
        }
    }

    public void launchGroups() {
       if(application != null) {
           application.goToGroups();
       }
    }

}
