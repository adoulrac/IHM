package IHM.controller;

import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable{

    private MainController application;

    @FXML
    private Button BtnDeconnect;
    @FXML
    private Button BtnGroupsHandling;
    @FXML
    private Button BtnPreferences;
    @FXML
    private Button BtnAddFriends;


    public WelcomeController()
    {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO
    }

    public void setApp(MainController application){
        this.application = application;
    }

}
