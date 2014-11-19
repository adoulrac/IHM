package IHM.controller;

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

    public MainController(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle(APP_NAME);
        gotoLogin();
        primaryStage.show();
    }

    private void gotoLogin() {
        try {
            LoginController login = (LoginController) replaceSceneContent("view/connexion.fxml");
            login.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
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
        gotoLogin();
    }

    private void gotoProfile() {
        try {
            ProfileController profile = (ProfileController) replaceSceneContent("IHM/view/config.fxml");
            //profile.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
