package IHM.controllers;

import DATA.interfaces.IHMtoDATA;
import DATA.internal.IHMtoDATAImpl;
import DATA.model.User;
import IHM.Main;
import IHM.interfaces.DATAtoIHM;
import IHM.interfaces.DATAtoIHMimpl;
import IHM.interfaces.IHMtoDATAstub;
import com.google.common.collect.Maps;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {

    private final static String APP_NAME = "PicShare";

    private final static String CSS_PATH = "IHM/resources/picshare.css";

    private Stage stage;

    private User currentUser;

    //TODO when goToSmwhere, remove entry from map
    private Map<Integer, Parent> requests;

    private int currentId;

    private IHMtoDATA DATAInterface;

    private DATAtoIHM DATAInterfaceReceiver;

    private Initializable currentController;

    public MainController(Stage primaryStage) {
        stage = primaryStage;
        currentId = 0;
        stage.setTitle(APP_NAME);
        requests = Maps.newHashMap();

        // Exchange commenting those to lines to use stub or DATA implementation
        //DATAInterface = new IHMtoDATAImpl();
        DATAInterface = new IHMtoDATAstub();
        DATAInterfaceReceiver = new DATAtoIHMimpl(this);

        goToLogin();
        primaryStage.show();
    }

    public void goToLogin() {
        try {
            LoginController login = (LoginController) replaceSceneContent("views/connexion.fxml");
            login.setApp(this);
            currentController = login;
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goToRegister() {
        try {
            RegisterController register = (RegisterController) replaceSceneContent("views/inscription.fxml");
            register.setApp(this);
            currentController = register;
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goToGroups() {
        try {
            GroupsController groups = (GroupsController) replaceSceneContent("views/gestion_groupes.fxml");
            groups.setApp(this);
            currentController = groups;
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
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
        scene.getStylesheets().add(CSS_PATH);
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
            ProfileController profile = (ProfileController) replaceSceneContent("views/config.fxml");
            profile.setApp(this);
            currentController = profile;
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goToWelcome() {
        try {
            WelcomeController welcome = (WelcomeController) replaceSceneContent("views/accueil.fxml");
            welcome.setApp(this);
            welcome.build();
            currentController = welcome;
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

    public void addRequest(Parent controller) {
        if(controller == null) {
            return;
        }
        this.requests.put(currentId++, controller);
    }

    public void removeRequest(Integer requestId) {
        if (requestId != null) {
            requests.remove(requestId);
        }
    }


    public Map<Integer, Parent> getRequests() {
        return requests;
    }

    public IHMtoDATA getIHMtoDATA() {
        return this.DATAInterface;
    }

    public Initializable getCurrentController() {
        return currentController;
    }

    public DATAtoIHM getDATAInterfaceReceiver() {
        return DATAInterfaceReceiver;
    }
}
