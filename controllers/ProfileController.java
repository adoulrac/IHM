package IHM.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Dialogs.DialogOptions;
import javafx.scene.control.Dialogs.DialogResponse;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

/**
 * @author Sylvain_
 *
 */

public class ProfileController implements Initializable {

	@FXML
	Label nickname;

	@FXML
	TextField avatarPath;

	@FXML
	Button changeAvatar;

	@FXML
	TextField lastname;

	@FXML
	TextField firstname;

	@FXML
	TextField birthdate;

	@FXML
	TextField newIP;

	@FXML
	Button validateNewIP;

	@FXML
	Button okButton;

	@FXML
	Button cancelButton;

	@FXML
	ImageView avatar;

	private MainController application;
	private String userFirstName, userLastName, userAvatar, userBirthDate,
			userNickName;
	private String defaultValue = "Unknown";
	private List<String> userIP;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		getUserInfos();
		displayUserInfo();
	}

	public void getUserInfos() {
		try {
			userLastName = application.currentUser().getLastname();
		} catch (Exception e) {
			Logger.getLogger(ProfileController.class.getName())
					.log(Level.SEVERE,
							"Lastname is empty or original value cannot be retrieved, changes will not be persisted");
			this.userLastName = defaultValue;
		}
		try {
			userFirstName = application.currentUser().getFirstname();
		} catch (Exception e) {
			Logger.getLogger(ProfileController.class.getName())
					.log(Level.SEVERE,
							"Fistname is empty or original value cannot be retrieved, changes will not be persisted");
			userFirstName = defaultValue;
		}
		try {
			userBirthDate = application.currentUser().getBirthDate();
		} catch (Exception e) {
			Logger.getLogger(ProfileController.class.getName())
					.log(Level.SEVERE,
							"Birthdate is empty or original value cannot be retrieved, changes will not be persisted");
			userBirthDate = defaultValue;
		}

		try {
			userAvatar = application.currentUser().getAvatar();
		} catch (Exception e) {
			Logger.getLogger(ProfileController.class.getName())
					.log(Level.SEVERE,
							"Avatar path is empty or original value cannot be retrieved, changes will not be persisted");
		}

		try {
			userNickName = application.currentUser().getLogin();
		} catch (Exception e) {
			Logger.getLogger(ProfileController.class.getName())
					.log(Level.SEVERE,
							"Login is empty or original value cannot be retrieved, changes will not be persisted");
			userNickName = defaultValue;
		}
		userIP = new ArrayList<String>();
		try {
			
			userIP.addAll(application.currentUser().getListIP());
		} catch (Exception e) {
			Logger.getLogger(ProfileController.class.getName())
					.log(Level.SEVERE,
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
		this.application.goToWelcome();
	}

	public void onOK() {
		if (hasInfoChanged()) {
			System.out.println("INFO HAS CHANGED");
			DialogResponse response = Dialogs.showConfirmDialog(application.getPrimaryStage(), "Changes have been detected on your profile, do you want to persist them?", 
			        "Save changes", "Save changes", DialogOptions.OK_CANCEL);
			if (response==DialogResponse.OK)
			{
				this.persistUserInfoChanges();
				this.application.goToWelcome();
			}
		} else {
			this.application.goToWelcome();
		}
	}
}
