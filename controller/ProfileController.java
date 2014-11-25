package IHM.controller;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import IHM.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Dialogs;

public class ProfileController implements Initializable {

	private MainController application;

	@FXML
	private Label nickname;

	@FXML
	private TextField avatarPath;

	@FXML
	private Button changeAvatar;

	@FXML
	private TextField name;

	@FXML
	private TextField lastname;

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

	public void setApp(MainController application) {
		this.application = application;
	}
	
	public void initialize() {
		
	}
	@FXML
	public void onChangeAvatar()
	{
		if (avatarPath.getText().equals(""))
		{
			Dialogs.showErrorDialog(application.getPrimaryStage(), "Avatar path cannot be empty.");
		}
		System.out.println("OnChangeAvatar");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
