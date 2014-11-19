package IHM.controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    private MainController application;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setApp(MainController application){
        this.application = application;
    }

}
