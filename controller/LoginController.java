package IHM.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import DATA.model.User;
import IHM.helper.ValidatorHelper;
import IHM.util.FileUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialogs;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import IHM.Main;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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

        if(ValidatorHelper.validateLogin(loginText) && ValidatorHelper.validatePassword(passwordText)) {
            //TODO: Authenticate user
            application.goToWelcome();
        } else {
            Dialogs.showErrorDialog(application.getPrimaryStage(), "Fields are not valid.");
        }
    }

    public void loadProfile() {
        File profileFile = FileUtil.chooseFile();
        //TODO: Import user profile

        User user = null;
        if(user != null) {
            application.setCurrentUser(user);
            Logger.getLogger(LoginController.class.getName()).log(Level.INFO, user.getLogin() + " has loaded his profile.");
            application.goToWelcome();
        } else {
            Dialogs.showInformationDialog(application.getPrimaryStage(), "Error in importing the user profile");
        }
    }

    public void register() {
        application.goToRegister();
    }

    public void setApp(MainController application){
        this.application = application;
    }
}
