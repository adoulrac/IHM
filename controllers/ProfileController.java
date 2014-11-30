package IHM.controllers;

import DATA.model.User;
import IHM.helpers.ValidatorHelper;
import IHM.utils.Dialogs;
import IHM.utils.FileUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
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
 * @author Sylvain_
 *
 */
public class ProfileController implements Initializable {

	@FXML
	private TitledPane profile;

	@FXML
	private Label nickname;

	@FXML
	private TextField avatarPath;

	@FXML
	private Button changeAvatar;

	@FXML
	private TextField lastname;

	@FXML
	private TextField firstname;

	@FXML
	private TextField birthdate;

	@FXML
	private TextField newIP;

	@FXML
	private Button validateNewIP;

	@FXML
	private Button okButton;

	@FXML
	private Button cancelButton;

	@FXML
	private ImageView avatar;
	
	@FXML
	private ListView listView;

	private MainController application;

	private String userFirstName, userLastName, userAvatar, userBirthDate,
			userNickName;

	private String defaultValue = "Unknown";

	private List<String> userIP;

	private User user;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// NOP
	}

	public void build(User userToDisplay) {
		this.user = userToDisplay;
		getUserInfos();
		displayUserInfo();
	}

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

	public void setApp(MainController application) {
		this.application = application;
	}

	public void avatarPicker() {
		File f = FileUtil.chooseFile();
		Image image = new Image(f.toURI().toString());
		avatar.setImage(image);
		avatarPath.setText(f.toURI().toString().replaceFirst("file:/", ""));
		Logger.getLogger(ProfileController.class.getName()).log(Level.INFO,
				"Avatar chnged");
	}

	public void displayUserInfo() {
		displayPicture();
		this.nickname.setText(this.userNickName);
		this.lastname.setText(this.userLastName);
		this.firstname.setText(this.userFirstName);
		this.birthdate.setText(this.userBirthDate);
		displayIPAddressesList();
	}

	public void displayPicture() {
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

    public void displayIPAddressesList() {
        ObservableList<String> addressesObservable = FXCollections.observableArrayList();
        for (String s : userIP) {
            addressesObservable.add(s);
        }
        listView.setItems(addressesObservable);
    }

    public void addIPAddress() {
        System.out.println("addIPAddress");
        if(!ValidatorHelper.validateIPs(newIP.getText())) {
        	 Dialogs.showErrorDialog("Incorrect address format.");
        }
        else {
            if (!userIP.contains(newIP.getText())){
                this.userIP.add(newIP.getText());
                try {
                    application.currentUser().setListIP(this.userIP);
                } catch (Exception e) {
                    Logger.getLogger(ProfileController.class.getName())
                            .log(Level.SEVERE,
                                    "New IP address cannot be persisted");
                }
            }
            else {
            	 Dialogs.showInformationDialog("You have already added this address. ");
            }
        }
        displayIPAddressesList();
    }

    public void removeIPAddress() {
    	// TODO
    }
	
	public boolean hasInfoChanged() {
		System.out.println("Info");
		if (!userFirstName.equals(this.nickname.getText())) {
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

	public void persistUserInfoChanges() {
		try {
			application.currentUser().setAvatar(avatarPath.getText());
			application.currentUser().setBirthDate(birthdate.getText());
			application.currentUser().setFirstname(firstname.getText());
			application.currentUser().setLastname(lastname.getText());
		} catch (Exception e) {
			Logger.getLogger(ProfileController.class.getName()).log(
					Level.SEVERE, "Unable to persist changes");
		}
	}

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

	public void onCancel() {
		((Stage) profile.getScene().getWindow()).close();
	}

	public void onOK() {
		removeNullValues();
		if (hasInfoChanged()) {
			boolean response = Dialogs.showConfirmationDialog("Would you like to save changes made to your profile ?");
            if (response){
				persistUserInfoChanges();
				((Stage) profile.getScene().getWindow()).close();
			}
		} else {
			((Stage) profile.getScene().getWindow()).close();
		}
	}
}
