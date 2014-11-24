package IHM.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.Inet4Address;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FriendsSubController implements Initializable{
    private MainController application;

    @FXML
    private AnchorPane allConnectedPane;

    public FriendsSubController() {
        super();
    }

    @Override
    public void initialize( URL url, ResourceBundle resourceBundle )
    {
        ArrayList<CheckBox> listTest = new ArrayList<CheckBox>();
        for(int i=0; i < 10; ++i)
        {
            CheckBox userCB = new CheckBox();
            userCB.setText( "Test " + i );
            listTest.add( userCB );
            allConnectedPane.getChildren().addAll( userCB );
        }
    }

    public void setApp(final MainController app) {
        this.application = app;
    }

    public void constructAllConnectedUser()
    {
        ArrayList<Inet4Address> connectedUsers = new ArrayList<Inet4Address>( this.application.currentUser().getListConnectedUser() );
        for(int i=0; i < connectedUsers.size(); ++i)
        {
            CheckBox userCB = new CheckBox();
            userCB.setText( connectedUsers.get( i ).getAddress().toString());
            allConnectedPane.getChildren().addAll( userCB );
        }
    }
}
