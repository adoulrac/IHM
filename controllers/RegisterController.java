package IHM.controllers;

import DATA.model.User;
import Dialogs.DialogFX;
import IHM.helpers.ValidatorHelper;
import IHM.utils.StringUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
        	DialogFX dialog = new DialogFX(DialogFX.Type.INFO);
	    	dialog.setMessage("Invalid Login: 3 to 15 characters with any lower case character, digit or special symbol only.");
	    	dialog.showDialog();
            return;
        }

        if(passwordText == null || confirmText == null || !passwordText.equals(confirmText)) {
        	DialogFX dialog = new DialogFX(DialogFX.Type.INFO);
	    	dialog.setMessage("Password are different.");
	    	dialog.showDialog();
            return;
        }

        if(!ValidatorHelper.validatePassword(passwordText)) {
        	DialogFX dialog = new DialogFX(DialogFX.Type.INFO);
	    	dialog.setMessage("Invalid Password: 6 to 20 characters string with at least one digit, one upper case letter, one lower case letter.");
	    	dialog.showDialog();
            return;
        }

        if(!ValidatorHelper.validateIPs(ipsText)) {
        	DialogFX dialog = new DialogFX(DialogFX.Type.INFO);
	    	dialog.setMessage("Invalid IP Addresses");
	    	dialog.showDialog();
            return;
        }

        User userToAdd = new User();
        userToAdd.setLogin(loginText);
        userToAdd.setPassword(passwordText);

        userToAdd.setListIP(StringUtil.toList(ipsText, StringUtil.SYSTEM_SEPARATOR));

        Logger.getLogger(RegisterController.class.getName()).log(Level.INFO, loginText + " is registering with password:" + passwordText + " and IPs:"+ ipsText.replace(StringUtil.SYSTEM_SEPARATOR, "|"));
        application.getIHMtoDATA().signup(userToAdd);
        User user = application.getIHMtoDATA().getCurrentUser();

        application.setCurrentUser(user);
        application.openWelcome();
    }


    public void setApp(MainController application){
        this.application = application;
    }
    
}
