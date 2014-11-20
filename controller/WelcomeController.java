package IHM.controller;

import java.util.ResourceBundle;

import IHM.Main;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        //TODO
    }

    public void setApp(final MainController app) {
        this.application = app;
    }

    public void launchPreferences() {
        this.application.goToProfile();
    }

    public void launchGroups() {
       this.application.goToGroups();
    }

}
