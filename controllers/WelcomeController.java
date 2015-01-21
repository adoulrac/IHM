package IHM.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.WindowEvent;
import IHM.utils.Dialogs;

/**
 * The Class WelcomeController.
 * Handles the welcome view.
 */
public class WelcomeController implements Initializable {

	/**
	 * The Constant AVATAR_DIM. Dimensions of the avatar image.
	 */
	private static final double AVATAR_DIM = 30.0;
	/**
	 * The application.
	 */
	private MainController application;
	/**
	 * The welcome pane.
	 */
	@FXML
	private Pane welcome;

	/**
	 * The tabbed pictures sub controller.
	 */
	@FXML
	private TabbedPicturesSubController tabbedPicturesSubController;

	/**
	 * The friends sub controller.
	 */
	@FXML
	private FriendsSubController friendsSubController;

	/**
	 * The user name (label).
	 */
	@FXML
	private Label lblUserName;

	/**
	 * The avatar of the user.
	 */
	@FXML
	private ImageView avatarUser;

	/**
	 * Instantiates a new welcome controller.
	 */
	public WelcomeController() {
		super();
	}

	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize
	 * (java.net.URL, java.util.ResourceBundle)
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
	 * Launch preferences view.
	 */
	public final void launchPreferences() {
		if (application != null) {
			application.goToProfile(application.currentUser());
		}
	}

	/**
	 * Launch groups view.
	 */
	public final void launchGroups() {
		if (application != null) {
			application.goToGroups();
		}
	}

	/**
	 * Handles the user logout.
	 */
	public final void logout() {
		try {
			if (application.currentUser() != null) {
				application.getIHMtoDATA().logout();
			}
		} catch (IOException e) {
			Logger.getLogger(WelcomeController.class.getName())
                    .log(Level.SEVERE, "Error in disconnecting the user.");
		}
		application.userLogout();
	}

	/**
	 * Builds the main components of the welcome view.
	 */
	public final void build() {
		if (application.currentUser() != null) {
			if (application.currentUser().getAvatar() != null) {
				Image f = application.currentUser().getAvatarImageObject();
				if (f != null) {
					avatarUser.setImage(f);
					avatarUser.setFitWidth(AVATAR_DIM);
					avatarUser.setFitHeight(AVATAR_DIM);
					avatarUser.setPreserveRatio(true);
					avatarUser.setSmooth(true);
					avatarUser.setCache(true);
				}
			}
			lblUserName.setText(" Connecté ("
                    + application.currentUser().getLogin() + ")");
		}

		// Build Pictures interface
		if (tabbedPicturesSubController != null) {
			tabbedPicturesSubController.setApp(application);
			tabbedPicturesSubController.build();
		} else {
			Logger.getLogger(WelcomeController.class.getName())
                    .log(Level.SEVERE, "Pictures controller is null.");
		}

		// Build Friends interface
		if (friendsSubController != null) {
			friendsSubController.setApp(application);
			friendsSubController.build();
		} else {
			Logger.getLogger(WelcomeController.class.getName())
                    .log(Level.SEVERE, "Friends sub controller is null.");
		}

		welcome.getScene().getWindow()
                .setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(final WindowEvent windowEvent) {
				logout();
			}
		});
	}

	/**
	 * Saves the modifications.
	 */
	public final void saveChanges() {
		try {
			application.getIHMtoDATA().save();
			Dialogs.showInformationDialog("Profil sauvegardé.");
		} catch (IOException e) {
			Dialogs.showErrorDialog("Erreur"
                    + " durant la sauvegarde du profil.");
            Logger.getLogger(WelcomeController.class.getName())
                    .log(Level.SEVERE, "Error in saving the profile.");
		}
	}

	/**
	 * Gets the friends sub controller.
	 *
	 * @return the friends sub controller
	 */
	public final FriendsSubController getFriendsSubController() {
		return friendsSubController;
	}

	/**
	 * Gets the tabbed pictures sub controller.
	 *
	 * @return the tabbed pictures sub controller
	 */
	public final TabbedPicturesSubController getTabbedPicturesSubController() {
		return tabbedPicturesSubController;
	}
}
