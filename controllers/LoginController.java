package IHM.controllers;

import DATA.model.User;
import IHM.helpers.ValidatorHelper;
import IHM.utils.FileUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialogs;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Login Controller.
 */
public class LoginController extends Pane implements Initializable {

    @FXML
    TextField login;
    @FXML
    PasswordField password;

    private MainController application;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //NOP
    }

    public void login() {
        String loginText = login.getText();
        String passwordText = password.getText();

        if(ValidatorHelper.validateLogin(loginText) &&
                ValidatorHelper.validatePassword(passwordText)
                && application.getIHMtoDATA().login(loginText, passwordText)) {
            openApplication();
        } else {
            Dialogs.showInformationDialog(application.getPrimaryStage(), "Login failed, please check if your password and login are correct.");
        }
    }

    public void loadProfile() {
        File profileFile = FileUtil.chooseFile();
        try {
            application.getIHMtoDATA().import_(profileFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        openApplication();
    }

    private void openApplication() {
        User user = application.getIHMtoDATA().getCurrentUser();
        if(user != null) {
            Logger.getLogger(LoginController.class.getName()).log(Level.INFO, "User " + user.getLogin() + " has logged in.");
            application.setCurrentUser(user);
            application.goToWelcome();
        } else {
            Dialogs.showErrorDialog(application.getPrimaryStage(), "Error in loading the current user.");
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, "Error in loading the current user.");
        }
    }

    public void register() {
        application.goToRegister();
    }

    public void setApp(MainController application){
        this.application = application;
    }
}
