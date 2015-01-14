package IHM.controllers;

import DATA.interfaces.IHMtoDATA;
import DATA.internal.IHMtoDATAImpl;
import DATA.model.User;
import IHM.Main;
import IHM.interfaces.DATAtoIHM;
import IHM.interfaces.DATAtoIHMimpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class MainController.
 */
public class MainController {

    /** The Constant APP_NAME. */
    private final static String APP_NAME = "PicShare";

    /** The Constant CSS_PATH. */
    private final static String CSS_PATH = "IHM/resources/picshare.css";

    /** The stage. */
    private Stage stage;

    /** The current user. */
    private User currentUser;

    /** The requests. */
    private Map<Integer, Initializable> requests;

    /** The current id. */
    private int currentId;

    /** The DATA interface. */
    private IHMtoDATA dataInterface;

    /** The DATA interface receiver. */
    private DATAtoIHM dataInterfaceReceiver;

    /** The current controller. */
    private Initializable currentController;

    /** The welcome controller. */
    private WelcomeController welcomeController;

    /** The new stages. */
    private List<Stage> newStages;

    /**
     * Instantiates a new main controller.
     *
     * @param primaryStage the primary stage
     */
    public MainController(Stage primaryStage) {
        stage = primaryStage;
        currentId = 0;
        stage.setTitle(APP_NAME);
        newStages = Lists.newArrayList();
        requests = Maps.newHashMap();

        stage.getIcons().add(new Image("IHM/resources/logo.jpeg"));
        goToLogin();
        primaryStage.show();
    }

    public void instanciateInterfaces() {
        dataInterfaceReceiver = new DATAtoIHMimpl(this);
        dataInterface = new IHMtoDATAImpl();
    }

    /**
     * Go to login.
     */
    public void goToLogin() {
        try {
            LoginController login = (LoginController) replaceSceneContent("views/connexion.fxml");
            login.setApp(this);
            login.build();
            removeAllRequests(currentController);
            currentController = login;
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Go to register.
     */
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

    /**
     * Go to groups.
     */
    public void goToGroups() {
        try {
            GroupsController groups = (GroupsController) replaceSceneContent("views/gestion_groupes.fxml", true);
            groups.setApp(this);
            groups.loadGroups();
            removeAllRequests(currentController);
            currentController = groups;
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Go to rules view.
     */
    public void goToRules() {
        try {
            RulesController rules = (RulesController) replaceSceneContent("views/gestion_droits.fxml", true);
            rules.setApp(this);
            rules.loadGroups();
            removeAllRequests(currentController);
            currentController = rules;
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Replace scene content.
     *
     * @param fxml the fxml
     * @param isNewStage the is new stage
     * @return the initializable
     * @throws Exception the exception
     */
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

    /**
     * Replace scene content.
     *
     * @param fxml the fxml
     * @return the initializable
     * @throws Exception the exception
     */
    private Initializable replaceSceneContent(String fxml) throws Exception {
        return replaceSceneContent(fxml, false);
    }

    /**
     * Builds the scene.
     *
     * @param fxml the fxml
     * @param loader the loader
     * @return the scene
     * @throws Exception the exception
     */
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

    /**
     * User logging.
     *
     * @param userId the user id
     * @param password the password
     * @return true, if successful
     */
    public boolean userLogging(String userId, String password){
        return true;
    }

    /**
     * User logout.
     */
    public void userLogout(){
        for(Stage stage : newStages)
            stage.close();
        resetCurrentUser();
        goToLogin();
    }

    /**
     * Reset current user.
     */
    private void resetCurrentUser() {
        currentUser = null;
    }

    /**
     * Go to profile.
     *
     * @param user the user
     */
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

    /**
     * Open welcome.
     */
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

    /**
     * Adds the request.
     *
     * @param controller the controller
     * @return the integer
     */
    public Integer addRequest(Initializable controller) {
        if(controller == null) {
            return null;
        }
        requests.put(++currentId, controller);
        return currentId;
    }

    /**
     * Removes the request.
     *
     * @param requestId the request id
     */
    public void removeRequest(Integer requestId) {
        if (requestId != null) {
            requests.remove(requestId);
        }
    }

    /**
     * Sets the current user.
     *
     * @param currentUser the new current user
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Current user.
     *
     * @return the user
     */
    public User currentUser() {
        return this.currentUser;
    }

    /**
     * Gets the primary stage.
     *
     * @return the primary stage
     */
    public Stage getPrimaryStage() {
        return this.stage;
    }

    /**
     * Gets the welcome controller.
     *
     * @return the welcome controller
     */
    public WelcomeController getWelcomeController() {
        return this.welcomeController;
    }

    /**
     * Removes the all requests.
     *
     * @param controller the controller
     */
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

    /**
     * Gets the requests.
     *
     * @return the requests
     */
    public Map<Integer, Initializable> getRequests() {
        return requests;
    }

    /**
     * Gets the IH mto data.
     *
     * @return the IH mto data
     */
    public IHMtoDATA getIHMtoDATA() {
        return this.dataInterface;
    }

    /**
     * Gets the current controller.
     *
     * @return the current controller
     */
    public Initializable getCurrentController() {
        return currentController;
    }

    /**
     * Gets the DATA interface receiver.
     *
     * @return the DATA interface receiver
     */
    public DATAtoIHM getDATAInterfaceReceiver() {
        return dataInterfaceReceiver;
    }

}
