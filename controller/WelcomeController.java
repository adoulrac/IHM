package IHM.controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    private MainController application;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO
    }

    public void setApp(MainController application){
        this.application = application;
    }
}
