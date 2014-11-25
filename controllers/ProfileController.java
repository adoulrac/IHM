package IHM.controllers;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    private MainController application;

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	// NOP
    }

    /**
     * @param application
     */
    public void setApp(MainController application){
        this.application = application;
    }

}
