package IHM.controllers;

import DATA.model.User;
import IHM.helpers.ValidatorHelper;
import IHM.utils.Dialogs;
import IHM.utils.StringUtil;
import IHM.validators.SimpleStringValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Class RegisterController.
 */
public class RegisterController implements Initializable {

    /**
     * The application.
     */
    private MainController application;

    /**
     * The login.
     */
    @FXML
    private TextField login;

    /**
     * The password.
     */
    @FXML
    private PasswordField password;

    /**
     * The confirmation.
     */
    @FXML
    private PasswordField confirmation;

    /**
     * The ips.
     */
    @FXML
    private TextArea ips;

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize
     * (java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        //NOP
    }

    /**
     * The user logs in.
     */
    public void login() {
        application.goToLogin();
    }

    /**
     * Handles the subscription.
     */
    public void subscribe() {
        String loginText = login.getText();
        String passwordText = password.getText();
        String confirmText = confirmation.getText();
        String ipsText = ips.getText();

        if (!ValidatorHelper.validateString(loginText)) {
            Dialogs.showInformationDialog(SimpleStringValidator.MESSAGE
                    + " Veuillez vérifier votre login.");
            return;
        }

        if (passwordText == null
                || confirmText == null
                || !passwordText.equals(confirmText)) {
            Dialogs.showInformationDialog("Les mots de passes sont différents.");
            return;
        }

        if (!ValidatorHelper.validateString(passwordText)) {
            Dialogs.showInformationDialog(SimpleStringValidator.MESSAGE
                    + " Veuillez vérifier votre mot de passe.");
            return;
        }

        if (!ValidatorHelper.validateIPs(ipsText)) {
            Dialogs.showInformationDialog("Adresse(s) IP invalide."
                    + " Veuillez vérifier le format.");
            return;
        }

        User userToAdd = new User();
        userToAdd.setLogin(loginText);
        userToAdd.setPassword(passwordText);
        userToAdd.setListIP(StringUtil.toList(ipsText,
                StringUtil.SYSTEM_SEPARATOR));

        application.getIHMtoDATA().signup(userToAdd);
        User user = application.getIHMtoDATA().getCurrentUser();

        application.setCurrentUser(user);
        application.openWelcome();
    }


    /**
     * Sets the app.
     *
     * @param app the new app
     */
    public void setApp(final MainController app) {
        this.application = app;
    }

}
