package IHM.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class WelcomeController.
 */
public class WelcomeController implements Initializable {

    /** The application. */
    private MainController application;

    /** The Constant AVATAR_DIM. */
    private static final double AVATAR_DIM = 30.0;

    /** The welcome. */
    @FXML
    private Pane welcome;

    /** The tabbed pictures sub controller. */
    @FXML
    private TabbedPicturesSubController tabbedPicturesSubController;

    /** The friends sub controller. */
    @FXML
    private FriendsSubController friendsSubController;

    /** The lbl user name. */
    @FXML
    private Label lblUserName;

    /** The avatar user. */
    @FXML
    private ImageView avatarUser;

    /**
     * Instantiates a new welcome controller.
     */
    public WelcomeController() {
        super();
    }

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        // NOP
    }

    /**
     * Sets the app.
     *
     * @param app the new app
     */
    public void setApp(final MainController app) {
        this.application = app;
    }

    /**
     * Launch preferences.
     */
    public void launchPreferences() {
        if(application != null) {
            application.goToProfile(application.currentUser());
        }
    }

    /**
     * Launch groups.
     */
    public void launchGroups() {
       if(application != null) {
           application.goToGroups();
       }
    }

    /**
     * Logout.
     */
    public void logout() {
        try
        {
            if( application.currentUser() != null )
                application.getIHMtoDATA().logout();
        } catch (IOException e)
        {
            Logger.getLogger( WelcomeController.class.getName() ).log( Level.SEVERE, "Error in disconnecting the user." );
        }
        application.userLogout();
    }

    /**
     * Builds the.
     */
    public void build() {
        if(application.currentUser() != null && application.currentUser().getAvatar() != null)
        {
            File f = new File(application.currentUser().getAvatar());
            if(f.isFile()) {
                avatarUser.setImage( new Image(f.toURI().toString()) );
                avatarUser.setFitWidth(AVATAR_DIM);
                avatarUser.setFitHeight(AVATAR_DIM);
                avatarUser.setPreserveRatio(true);
                avatarUser.setSmooth(true);
                avatarUser.setCache(true);
            }
            lblUserName.setText( " Connecté (" + application.currentUser().getLogin() + ")" );
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

    /**
     * Gets the friends sub controller.
     *
     * @return the friends sub controller
     */
    public FriendsSubController getFriendsSubController() {
        return friendsSubController;
    }

    /**
     * Gets the tabbed pictures sub controller.
     *
     * @return the tabbed pictures sub controller
     */
    public TabbedPicturesSubController getTabbedPicturesSubController() {
        return tabbedPicturesSubController;
    }
}
