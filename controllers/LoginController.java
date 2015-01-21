package IHM.controllers;

import DATA.model.User;
import IHM.helpers.ValidatorHelper;
import IHM.utils.Dialogs;
import IHM.utils.FileUtil;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Login Controller.
 */
public class LoginController extends Pane implements Initializable {

    /**
     * The login.
     */
    @FXML
    TextField login;

    /**
     * The password.
     */
    @FXML
    PasswordField password;

    /**
     * The application.
     */
    private MainController application;

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //NOP
    }

    /**
     * Build: adds listener to Enter key.
     */
    public void build() {
        EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    login();
                }
            }
        };
        login.setOnKeyPressed(eventHandler);
        password.setOnKeyPressed(eventHandler);
    }

    /**
     * Login.
     */
    public void login() {
        String loginText = login.getText();
        String passwordText = password.getText();

        if (ValidatorHelper.validateString(loginText) &&
                ValidatorHelper.validateString(passwordText)
                && application.getIHMtoDATA().login(loginText, passwordText)) {
            openApplication();
        } else {
            Dialogs.showInformationDialog("Login failed, please check if your password and login are correct.");
        }
    }

    /**
     * Load profile.
     */
    public void loadProfile() {
        File profileFile = FileUtil.chooseFile();
        try {
            application.getIHMtoDATA().import_(profileFile.getAbsolutePath());
            openApplication();
        } catch (Exception e) {
            Dialogs.showErrorDialog("Error in loading the profile file");
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, "Erreur durant le chargement du profil.");
        }
    }

    /**
     * Open application.
     */
    private void openApplication() {
        User user = application.getIHMtoDATA().getCurrentUser();
        if (user != null) {
            Logger.getLogger(LoginController.class.getName()).log(Level.INFO, "User " + user.getLogin() + " has logged in.");
            application.setCurrentUser(user);
            application.openWelcome();
        } else {
            Dialogs.showErrorDialog("Error while loading the current user");
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, "Erreur durant le chargement de l'utilisateur");
        }
    }

    /**
     * Register.
     */
    public void register() {
        application.goToRegister();
    }

    /**
     * Sets the app.
     *
     * @param application the new app
     */
    public void setApp(MainController application) {
        this.application = application;
    }
}
