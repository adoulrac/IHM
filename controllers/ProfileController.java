package IHM.controllers;

import DATA.model.User;
import IHM.helpers.ValidatorHelper;
import IHM.utils.Dialogs;
import IHM.utils.FileUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class ProfileController.
 *
 * @author Sylvain_
 */
public class ProfileController implements Initializable {

	/** The profile. */
	@FXML
	private TitledPane profile;

	/** The nickname. */
	@FXML
	private Label nickname;

	/** The avatar path. */
	@FXML
	private TextField avatarPath;

	/** The change avatar button. */
	@FXML
	private Button changeAvatar;

	/** The lastname. */
	@FXML
	private TextField lastname;

	/** The firstname. */
	@FXML
	private TextField firstname;

	/** The birthdate. */
	@FXML
	private TextField birthdate;

	/** The new ip. */
	@FXML
	private TextField newIP;

	/** The validate new ip. */
	@FXML
	private Button validateNewIP;

	/** The ok button. */
	@FXML
	private Button okButton;

	/** The cancel button. */
	@FXML
	private Button cancelButton;

	/** The avatar. */
	@FXML
	private ImageView avatar;

	/** The list view. */
	@FXML
	private ListView<String> listView;

	/** The remove button. */
	@FXML
	private Button removeButton;
	
	/** The IP panel. */
	@FXML
	private TitledPane IPPanel;
	
	/** The application. */
	private MainController application;

	/** The user nick name. */
	private String userFirstName, userLastName, userAvatar, userBirthDate,
			userNickName;

	/** The default value. */
	private String defaultValue = "Unknown";

	/** The user ip. */
	private List<String> userIP;

	/** The user. */
	private User user;

	/** The editable. */
	private boolean editable=false;
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// NOP
	}

	/**
	 * Builds and initializes the content.
	 *
	 * @param userToDisplay the user to display
	 */
	public void build(User userToDisplay) {
		this.user = userToDisplay;
		if (userToDisplay.getUid().equals(application.currentUser().getUid())) {
			editable = true;
		}
		if (!isEditable()) {
			removeButton.setVisible(false);
			okButton.setVisible(false);
			changeAvatar.setVisible(false);
		}
		getUserInfos();
		displayUserInfo();
		removeButton.setDisable(true);
		listView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> arg0,
							String arg1, String arg2) {
						removeButton.setDisable(false);
					}
				});
	}

	/**
	 * Gets the user infos.
	 *
	 * @return the user infos
	 */
	public void getUserInfos() {
		try {
			userLastName = user.getLastname();
		} catch (Exception e) {
			Logger.getLogger(ProfileController.class.getName())
					.log(Level.INFO,
							"Lastname is empty or original value cannot be retrieved, changes will not be persisted");
			this.userLastName = defaultValue;
		}
		try {
			userFirstName = user.getFirstname();
		} catch (Exception e) {
			Logger.getLogger(ProfileController.class.getName())
					.log(Level.INFO,
							"Fistname is empty or original value cannot be retrieved, changes will not be persisted");
			userFirstName = defaultValue;
		}
		try {
			userBirthDate = user.getBirthDate();
		} catch (Exception e) {
			Logger.getLogger(ProfileController.class.getName())
					.log(Level.INFO,
							"Birthdate is empty or original value cannot be retrieved, changes will not be persisted");
			userBirthDate = defaultValue;
		}

		try {
			userAvatar = user.getAvatar();
		} catch (Exception e) {
			Logger.getLogger(ProfileController.class.getName())
					.log(Level.INFO,
							"Avatar path is empty or original value cannot be retrieved, changes will not be persisted");
		}

		try {
			userNickName = user.getLogin();
		} catch (Exception e) {
			Logger.getLogger(ProfileController.class.getName())
					.log(Level.INFO,
							"Login is empty or original value cannot be retrieved, changes will not be persisted");
			userNickName = defaultValue;
		}
		userIP = new ArrayList<String>();
		try {

			userIP.addAll(user.getListIP());
		} catch (Exception e) {
			Logger.getLogger(ProfileController.class.getName())
					.log(Level.INFO,
							"List of addresses is empty or original value cannot be retrieved, changes will not be persisted");
		}

	}

	/**
	 * Sets the app.
	 *
	 * @param application the new app
	 */
	public void setApp(MainController application) {
		this.application = application;
	}

	/**
	 * Checks if the user is editable.
	 *
	 * @return true, if is editable
	 */
	public boolean isEditable() {
		return editable;
	}
	
	/**
	 * Display Avatar picker and persists data.
	 */
	public void avatarPicker() {
		File f = null;
		try {
			f = FileUtil.chooseFile();
			Image image = new Image(f.toURI().toString());
			avatar.setImage(image);
			avatarPath.setText(f.toURI().toString().replaceFirst("file:/", ""));
			if (isEditable()) {
				application.currentUser().setAvatar(avatarPath.getText());
			}
			Logger.getLogger(ProfileController.class.getName()).log(Level.INFO,
					"Avatar changed");
		} catch (Exception e) {
		}
	}

	/**
	 * Display user info.
	 */
	public void displayUserInfo() {
		displayUserAvatar();
		this.nickname.setText(this.userNickName);
		this.lastname.setText(this.userLastName);
		this.firstname.setText(this.userFirstName);
		this.birthdate.setText(this.userBirthDate);
		this.avatarPath.setText(this.userAvatar);
		displayIPAddressesList();
	}

	/**
	 * Display the user's avatar.
	 */
	public void displayUserAvatar() {
		try {
			String userAvatarPath = userAvatar;
			File file = new File(userAvatarPath);
			Image image = new Image(file.toURI().toString());
			avatar.setImage(image);

		} catch (Exception e) {
			Logger.getLogger(ProfileController.class.getName()).log(
					Level.SEVERE, "Unknown User avatar");
		}
	}

	/**
	 * Display ip addresses list.
	 */
	public void displayIPAddressesList() {
		ObservableList<String> addressesObservable = FXCollections
				.observableArrayList();
		for (String s : userIP) {
			addressesObservable.add(s);
		}
		listView.setItems(addressesObservable);
	}

	/**
	 * Adds an ip address and persists changes
	 */
	public void addIPAddress() {
		if (!ValidatorHelper.validateIPs(newIP.getText())) {
			Dialogs.showErrorDialog("Incorrect address format.");
		} else {
			if (!userIP.contains(newIP.getText()) && isEditable()) {
				this.userIP.add(newIP.getText());
				try {
					application.currentUser().setListIP(this.userIP);
				} catch (Exception e) {
					Logger.getLogger(ProfileController.class.getName()).log(
							Level.SEVERE, "New IP address cannot be persisted");
				}
			} else {
				Dialogs.showInformationDialog("You have already added this address. ");
			}
		}
		displayIPAddressesList();
	}

	/**
	 * Removes the selected ip address.
	 */
	public void removeIPAddress() {
		int selectedId = listView.getSelectionModel().getSelectedIndex();
		if (selectedId != -1) {
			int newSelectedId = (selectedId == listView.getItems().size() - 1) ? selectedId - 1
					: selectedId;
			listView.getItems().remove(selectedId);
			listView.getSelectionModel().select(newSelectedId);
			userIP.remove(listView.getItems().get(selectedId));
			application.currentUser().setListIP(userIP);
		}
	}

	/**
	 * Checks for info changed.
	 *
	 * @return true, if info has changed, else return false
	 */
	public boolean hasInfoChanged() {
		if (!userFirstName.equals(this.firstname.getText())) {
			return true;
		}
		if (!userLastName.equals(this.lastname.getText())) {
			return true;
		}
		if (!userBirthDate.equals(this.birthdate.getText())) {
			return true;
		}
		return false;
	}

	/**
	 * Persist user info changes.
	 */
	public void persistUserInfoChanges() {
		if (!isEditable()) { return; }
		try {
			application.currentUser().setBirthDate(birthdate.getText());
			application.currentUser().setFirstname(firstname.getText());
			application.currentUser().setLastname(lastname.getText());
		} catch (Exception e) {
			Logger.getLogger(ProfileController.class.getName()).log(
					Level.SEVERE, "Unable to persist changes");
		}
	}

	/**
	 * Replace null values by empty string to avoid issues.
	 */
	public void removeNullValues() {
		if (userFirstName == null) {
			userFirstName = "";
		}
		if (userLastName == null) {
			userLastName = "";
		}
		if (userBirthDate == null) {
			userLastName = "";
		}
		if (userAvatar == null) {
			userAvatar = "";
		}
	}

	/**
	 * On cancel: return to main window.
	 */
	public void onCancel() {
		((Stage) profile.getScene().getWindow()).close();
	}

	/**
	 * On ok: persists changes if needed, then return to main window.
	 */
	public void onOK() {
		removeNullValues();
		if (hasInfoChanged()) {
			boolean response = Dialogs
					.showConfirmationDialog("Would you like to save changes made to your profile ?");
			if (response) {
				persistUserInfoChanges();
				((Stage) profile.getScene().getWindow()).close();
			}
		} else {
			((Stage) profile.getScene().getWindow()).close();
		}
	}
}