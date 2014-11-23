package IHM.controller;

import DATA.model.User;
import IHM.Main;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {

    private final static String APP_NAME = "PicShare";
    private Stage stage;
    private User currentUser;

    public MainController(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle(APP_NAME);
        goToLogin();
        primaryStage.show();
    }

    public void goToLogin() {
        try {
            LoginController login = (LoginController) replaceSceneContent("view/connexion.fxml");
            login.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goToRegister() {
        try {
            RegisterController register = (RegisterController) replaceSceneContent("view/inscription.fxml");
            register.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goToGroups() {
        try {
            GroupsController groups = (GroupsController) replaceSceneContent("view/gestion_groupes.fxml");
            groups.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        Parent page;
        try {
            page = (Parent) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page);
        scene.getStylesheets().add("IHM/resource/picshare.css");
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    public boolean userLogging(String userId, String password){
        return true;
    }

    public void userLogout(){
        goToLogin();
    }

    public void goToProfile() {
        try {
            ProfileController profile = (ProfileController) replaceSceneContent("view/config.fxml");
            profile.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goToWelcome() {
        try {
            WelcomeController welcome = (WelcomeController) replaceSceneContent("view/accueil.fxml");
            welcome.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User currentUser() {
        return this.currentUser;
    }

    public Stage getPrimaryStage() {
        return this.stage;
    }
}
