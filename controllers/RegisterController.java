package IHM.controllers;

import DATA.model.User;
import IHM.helpers.ValidatorHelper;
import IHM.utils.IPAddressUtil;
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

        if(!ValidatorHelper.validateLogin(loginText)) {
            Dialogs.showInformationDialog(application.getPrimaryStage(), "Invalid Login: 3 to 15 characters with any lower case character, digit or special symbol â€œ_-â€ only.");
            return;
        }

        if(passwordText == null || confirmText == null || !passwordText.equals(confirmText)) {
            Dialogs.showInformationDialog(application.getPrimaryStage(), "Passwords are different.");
            return;
        }

        if(!ValidatorHelper.validatePassword(passwordText)) {
            Dialogs.showInformationDialog(application.getPrimaryStage(), "Invalid Password: 6 to 20 characters string with at least one digit, one upper case letter, one lower case letter.");
            return;
        }

        if(!ValidatorHelper.validateIPs(ipsText)) {
            Dialogs.showInformationDialog(application.getPrimaryStage(), "Invalid IP Addresses.");
            return;
        }

        User userToAdd = new User();
        userToAdd.setLogin(loginText);
        userToAdd.setPassword(passwordText);
        //userToAdd.setListIP(IPAddressUtil.toInet4AddressList(ipsText));

        application.getIHMtoDATA().signup(userToAdd);
        Logger.getLogger(RegisterController.class.getName()).log(Level.INFO, loginText + " is registering.");
        User user = application.getIHMtoDATA().getCurrentUser();

        application.setCurrentUser(user);
        application.goToWelcome();
    }


    public void setApp(MainController application){
        this.application = application;
    }
    
}
