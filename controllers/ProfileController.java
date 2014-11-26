package IHM.controllers;

import DATA.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Dialogs.DialogOptions;
import javafx.scene.control.Dialogs.DialogResponse;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import IHM.utils.FileUtil;
import javafx.stage.Stage;

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

	private MainController application;

	private String userFirstName, userLastName, userAvatar, userBirthDate, userNickName;

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
		Logger.getLogger(ProfileController.class.getName()).log(Level.INFO, "Avatar chnged");
	}

	public void displayUserInfo() {
		displayPicture();
		this.nickname.setText(this.userNickName);
		this.lastname.setText(this.userLastName);
		this.firstname.setText(this.userFirstName);
		this.birthdate.setText(this.userBirthDate);
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

	public boolean hasInfoChanged() {
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
		}
		catch (Exception e){
			Logger.getLogger(ProfileController.class.getName())
			.log(Level.SEVERE,
					"Unable to persist changes");
		}
		
	}

	public void onCancel() {
        ((Stage) profile.getScene().getWindow()).close();
	}

	public void onOK() {
		if (hasInfoChanged()) {
			System.out.println("INFO HAS CHANGED");
			DialogResponse response = Dialogs.showConfirmDialog(application.getPrimaryStage(), "Changes have been detected on your profile, do you want to persist them?", 
			        "Save changes", "Save changes", DialogOptions.OK_CANCEL);
			if (response==DialogResponse.OK)
			{
				persistUserInfoChanges();
                ((Stage) profile.getScene().getWindow()).close();
			}
		} else {
            ((Stage) profile.getScene().getWindow()).close();
		}
	}
}
