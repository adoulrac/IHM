package IHM.controllers;

import DATA.interfaces.IHMtoDATA;
import DATA.internal.IHMtoDATAImpl;
import DATA.model.User;
import IHM.Main;
import IHM.interfaces.DATAtoIHM;
import IHM.interfaces.DATAtoIHMimpl;
import IHM.interfaces.IHMtoDATAstub;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    private Map<Integer, Parent> requests;

    private int currentId;

    private IHMtoDATA DATAInterface;

    private DATAtoIHM DATAInterfaceReceiver;

    private Initializable currentController;

    private WelcomeController welcomeController;

    private List<Stage> newStages;

    public MainController(Stage primaryStage) {
        stage = primaryStage;
        currentId = 0;
        stage.setTitle(APP_NAME);
        newStages = Lists.newArrayList();
        requests = Maps.newHashMap();
        DATAInterface = new IHMtoDATAImpl();
        DATAInterfaceReceiver = new DATAtoIHMimpl(this);

        goToLogin();
        primaryStage.show();
    }

    public void goToLogin() {
        try {
            LoginController login = (LoginController) replaceSceneContent("views/connexion.fxml");
            login.setApp(this);
            removeAllRequests(currentController);
            currentController = login;
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goToRegister() {
        try {
            RegisterController register = (RegisterController) replaceSceneContent("views/inscription.fxml");
            register.setApp(this);
            removeAllRequests(currentController);
            currentController = register;
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goToGroups() {
        try {
            GroupsController groups = (GroupsController) replaceSceneContent("views/gestion_groupes.fxml", true);
            groups.setApp(this);
            removeAllRequests(currentController);
            currentController = groups;
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Initializable replaceSceneContent(String fxml, boolean isNewStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Scene scene = buildScene(fxml, loader);
        Stage currentStage = isNewStage ? new Stage() : stage;
        currentStage.setScene(scene);
        // currentStage.initStyle(StageStyle.UNDECORATED);
        currentStage.setResizable(false);
        currentStage.sizeToScene();
        if(isNewStage) {
            currentStage.show();
            currentStage.toFront();
            newStages.add(currentStage);
        }
        return (Initializable) loader.getController();
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        return replaceSceneContent(fxml, false);
    }

    private Scene buildScene(String fxml, FXMLLoader loader) throws Exception {
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
        return scene;
    }

    public boolean userLogging(String userId, String password){
        return true;
    }

    public void userLogout(){
        for(Stage stage : newStages)
            stage.close();
        resetCurrentUser();
        goToLogin();
    }

    private void resetCurrentUser() {
        currentUser = null;
    }

    public void goToProfile(User user) {
        try {
            ProfileController profile = (ProfileController) replaceSceneContent("views/config.fxml", true);
            profile.setApp(this);
            profile.build(user);
            removeAllRequests(currentController);
            currentController = profile;
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void openWelcome() {
        try {
            WelcomeController welcome = (WelcomeController) replaceSceneContent("views/accueil.fxml");
            welcome.setApp(this);
            welcome.build();
            welcomeController = welcome;
            removeAllRequests(welcomeController);
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User currentUser() {
        return this.currentUser;
    }

    public Stage getPrimaryStage() {
        return this.stage;
    }

    public WelcomeController getWelcomeController() {
        return this.welcomeController;
    }

    public void removeAllRequests(Initializable controller) {
        List<Parent> allControllers = new ArrayList<>();
        if (controller instanceof WelcomeController) {
            allControllers.add(((WelcomeController) controller).getFriendsSubController());
            allControllers.add(((WelcomeController) controller).getTabbedPicturesSubController());
        }
        for (Map.Entry item : requests.entrySet()) {
            if (allControllers.contains(item.getValue())) {
                requests.remove(item.getKey());
            }
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
