package IHM.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import DATA.model.User;
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
    TextField userId;
    @FXML
    PasswordField password;
    @FXML
    Button login;

    private MainController application;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //NOP
    }
    
    public void login() {
    }

    public void loadProfile() {
        File profileFile = FileUtil.chooseFile();
        //TODO: IHMtoDATA.importProfile(profileFile);
        User user = null;
        if(user != null) {
            application.setCurrentUser(user);
            application.goToWelcome();
        } else {
            Dialogs.showInformationDialog(application.getPrimaryStage(), "Error in importing the user profile");
        }
    }

    public void register() {
        if(application != null) {
            application.goToRegister();
        }
    }

    public void setApp(MainController application){
        this.application = application;
    }
}
