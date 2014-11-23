package IHM.controller;

import DATA.model.User;
import IHM.Main;
import IHM.helper.ValidatorHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialogs;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterController implements Initializable {

    private MainController application;

    @FXML
    private TextField login;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmation;

    @FXML
    private TextArea ips;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //NOP
    }

    public void login() {
        application.goToLogin();
    }

    public void subscribe() {
        String loginText = login.getText();
        String passwordText = password.getText();
        String confirmText = confirmation.getText();
        String ipsText = ips.getText();

        if(passwordText == null || confirmText == null || !passwordText.equals(confirmText)) {
            Dialogs.showErrorDialog(application.getPrimaryStage(), "Passwords are different.");
            return;
        }

        if(!ValidatorHelper.validateLogin(loginText)
                || !ValidatorHelper.validatePassword(passwordText)
                || !ValidatorHelper.validateIPs(ipsText)){
            Dialogs.showErrorDialog(application.getPrimaryStage(), "Fields are not valid.");
            return;
        }

        // TODO: Add the new user
        Logger.getLogger(RegisterController.class.getName()).log(Level.INFO, loginText + " is registering.");
        User user = null;

        application.setCurrentUser(user);
        application.goToWelcome();
    }

    public void setApp(MainController application){
        this.application = application;
    }
}
