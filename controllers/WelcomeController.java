package IHM.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WelcomeController implements Initializable {

    private MainController application;

    @FXML
    private Pane welcome;

    @FXML
    private TabbedPicturesSubController tabbedPicturesSubController;

    @FXML
    private FriendsSubController friendsSubController;

    @FXML
    private Label lblUserName;

    public WelcomeController() {
        super();
    }

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        // NOP
    }

    public void setApp(final MainController app) {
        this.application = app;
    }

    public void launchPreferences() {
        if(application != null) {
            application.goToProfile(application.currentUser());
        }
    }

    public void launchGroups() {
       if(application != null) {
           application.goToGroups();
       }
    }

    public void logout() {
        try {
            if(application.currentUser() != null)
                application.getIHMtoDATA().logout();
        } catch (IOException e) {
            Logger.getLogger( WelcomeController.class.getName() ).log( Level.SEVERE, "Error in disconnecting the user." );
        }
        application.userLogout();
    }

    public void build() {
        if(application.currentUser() != null)
        {
            lblUserName.setText( "Connecté (" + application.currentUser().getLogin() + ")" );
        }

        // Build Pictures interface
        if(tabbedPicturesSubController != null) {
            tabbedPicturesSubController.setApp(application);
            tabbedPicturesSubController.build();
        }
        else
        {
            Logger.getLogger( WelcomeController.class.getName() ).log( Level.SEVERE, "Pictures controller is null." );
        }

        // Build Friends interface
        if(friendsSubController != null) {
            friendsSubController.setApp(application);
            friendsSubController.build();
        }
        else
        {
            Logger.getLogger( WelcomeController.class.getName() ).log( Level.SEVERE, "Friends sub controller is null." );
        }

        ((Stage) welcome.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                logout();
            }
        });
    }

    public FriendsSubController getFriendsSubController() {
        return friendsSubController;
    }

    public TabbedPicturesSubController getTabbedPicturesSubController() {
        return tabbedPicturesSubController;
    }
}
