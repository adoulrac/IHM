package IHM.controllers;

import DATA.model.Group;
import DATA.model.User;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * The Class GroupsController.
 */
public class GroupsController implements Initializable {
    
    /** The groups. */
    @FXML
    private ListView groups;
    
    /** The members. */
    @FXML
    private ListView members;
    
    /** The group selected. */
    @FXML
    private TextField groupSelected;
    
    /** The new group name. */
    @FXML
    private TextField newGroupName;
    
    /** The add group btn. */
    @FXML
    private Button addGroupBtn;
    
    /** The add user name. */
    @FXML
    private TextField addUserName;
    
    /** The add user btn. */
    @FXML
    private Button addUserBtn;
    
    /** The delete group btn. */
    @FXML
    private Button deleteGroupBtn;


    /** The application. */
    private MainController application;

    /** The obs groups list. */
    private final ObservableList obsGroupsList= observableArrayList();
    
    /** The obs members list. */
    private final ObservableList obsMembersList= observableArrayList();

    //List<Group> listGroups = null;
    /** The list groups. */
    List<Group> listGroups = new ArrayList<Group>();
    
    /** The list users. */
    List<User> listUsers = new ArrayList<User>();
    
    /** The list users2. */
    List<User> listUsers2 = new ArrayList<User>();
    
    /** The amis. */
    Group amis = new Group("Amis");
    
    /** The famille. */
    Group famille = new Group("Famille");
    
    /** The travail. */
    Group travail = new Group("Travail");
    
    /** The arthur. */
    User arthur = new User("avanceul", "password", "Arthur", "Van Ceulen", "arthur.jpg", "16/08/1991");
    
    /** The rachid. */
    User rachid = new User("adoulrac", "password", "Rachid", "Adoul", "rachid.jpg", "08/06/1992");
    
    /** The selim. */
    User selim = new User("szenagui", "password", "Selim", "Zenagui", "selim.jpg", "08/06/1992");
    
    /** The arthurt. */
    User arthurt = new User("tranarth", "password", "Arthur", "Tran", "arthurt.jpg", "08/06/1992");




    /**
     * Sets the app.
     *
     * @param application the new app
     */
    public void setApp(MainController application){
        this.application = application;
    }

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        newGroupName.setPromptText("Ajouter groupe...");
        addUserName.setPromptText("Ajouter membre...");
        /*
        groups.setCellFactory(new Callback<ListView<String>, ListCell>() {
            @Override
            public ListCell call(ListView<String> listView) {
                return new ButtonListCell();
            }
        });*/

    }

    /**
     * Load groups.
     */
    public void loadGroups() {

        disableFields(true);

        try {
            //listGroups = this.application.currentUser().getListGroups();
            listUsers.add(arthur);
            listUsers.add(rachid);
            listUsers2.add(selim);
            listUsers2.add(arthurt);
            amis.setUsers(listUsers);
            famille.setUsers(listUsers2);
            travail.setUsers(listUsers);
            listGroups.add(amis);
            listGroups.add(famille);
            listGroups.add(travail);
            arthur.setListGroups(listGroups);
            rachid.setListGroups(listGroups);
            selim.setListGroups(listGroups);
            arthurt.setListGroups(listGroups);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        groups.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    if (!groups.getSelectionModel().getSelectedItem().equals(null)) {
                        disableFields(false);
                        if (!listGroups.isEmpty()) {
                            displayUsers(groups.getSelectionModel().getSelectedItem().toString());
                            groupSelected.setText(groups.getSelectionModel().getSelectedItem().toString());
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        groups.setItems(obsGroupsList);

        if(listGroups != null) {
            for (Group g : listGroups) {
                obsGroupsList.add(g.getNom());
            }
        }
    }

    /**
     * Change group name.
     *
     * @param event the event
     */
    @FXML
    private void changeGroupName(ActionEvent event) {
        //application.getIHMtoDATA().addGroup(new Group(groupSelected.getText()));
        obsGroupsList.set(groups.getSelectionModel().getSelectedIndex(),groupSelected.getText());
    }


    /**
     * Delete group.
     *
     * @param event the event
     */
    @FXML
    private void deleteGroup(ActionEvent event) {
        loop :
        for (Group g : listGroups) {
            if(g.getNom().equals(groups.getSelectionModel().getSelectedItem())) {
                //application.getIHMtoDATA().deleteGroup(g);
                listGroups.remove(g);
                obsGroupsList.remove(groups.getSelectionModel().getSelectedIndex());
                groupSelected.clear();
                disableFields(true);
                break loop;
            }
        }
        obsMembersList.clear();
        members.setItems(obsMembersList);
    }

    /**
     * Adds the new group.
     *
     * @param event the event
     */
    @FXML
    public void addNewGroup(ActionEvent event){
        //application.getIHMtoDATA().addGroup(new Group(newGroupName.getText()));
        obsGroupsList.add(newGroupName.getText());
        newGroupName.clear();
    }

    /**
     * Display users.
     *
     * @param groupName the group name
     */
    public void displayUsers(String groupName) {
        if(groupName.equals(null)) {
            obsMembersList.clear();
        }
        else {
            obsMembersList.clear();
            for (Group g : listGroups) {
                if (g.getNom().equals(groupName)) {
                    for (User u : g.getUsers()) {
                        obsMembersList.add(u.getFirstname() + " " + u.getLastname());
                    }
                }
            }
        }
        members.setItems(obsMembersList);
    }

    /**
     * Disable fields.
     *
     * @param b the b
     */
    public void disableFields (Boolean b) {
        groupSelected.setDisable(b);
        deleteGroupBtn.setDisable(b);
        addUserName.setDisable(b);
        addUserBtn.setDisable(b);
        members.setDisable(b);
    }
}
