package IHM.controllers;

import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.net.URL;

public class WelcomeController extends Pane implements Initializable {

    private MainController application;

    @FXML
    private TabbedPicturesSubController picturesSubController;

    @FXML
    private Button btnDeconnect;
    @FXML
    private Button btnGroupsHandling;
    @FXML
    private Button btnPreferences;
    @FXML
    private Button btnAddFriends;

    @FXML
    private Label lblUserName;

    public WelcomeController()
    {

    }

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

    public void logout() {
        this.application.userLogout();
    }

    public void updateView()
    {
        lblUserName.setText( "Connecté (" + this.application.currentUser().getLogin() + ")");
        picturesSubController.setApp( application );
        //picturesSubController.constructAllImg();
        //picturesSubController.constructMyImg();
    }

}
