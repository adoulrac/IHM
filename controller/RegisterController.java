package IHM.controller;

import IHM.Main;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    private MainController application;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //NOP
    }

    public void login() {
        if(application != null) {
            application.goToLogin();
        }
    }

    public void setApp(MainController application){
        this.application = application;
    }
}
