package IHM.controllers;

import DATA.interfaces.IHMtoDATA;
import DATA.model.Group;
import DATA.model.User;
import IHM.interfaces.IHMtoDATAstub;
import IHM.utils.Dialogs;
import com.sun.org.apache.xpath.internal.operations.Bool;
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
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;

import static IHM.utils.Dialogs.showErrorDialog;
import static IHM.utils.Dialogs.showInformationDialog;
import static IHM.utils.Dialogs.showWarningDialog;
import static javafx.collections.FXCollections.observableArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class GroupsController.
 */
public class GroupsController implements Initializable {
    
    /** The groups. */
    @FXML
    private TitledPane gestionGroupes;

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

    @FXML
    private Button deleteMemberBtn;

    @FXML
    private Button finishBtn;

    /** The application. */
    private MainController application;

    /** The obs groups list. */
    private final ObservableList obsGroupsList= observableArrayList();
    
    /** The obs members list. */
    private final ObservableList obsMembersList= observableArrayList();


    List<Group> listGroups = null;
    IHMtoDATA stub = new IHMtoDATAstub();

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

        newGroupName.setPromptText("Add group...");
        addUserName.setPromptText("Add member...");
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
        deleteMemberBtn.setDisable(true);

        try {
            //listGroups = application.getIHMtoDATA().getGroups();
            listGroups = stub.getGroups();
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
                    deleteMemberBtn.setDisable(true);
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

        members.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            try {
                if (!members.getSelectionModel().getSelectedItem().equals(null)) {
                    deleteMemberBtn.setDisable(false);
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
    public void changeGroupName(ActionEvent event) {
        //application.getIHMtoDATA().addGroup(new Group(groupSelected.getText()));
        obsGroupsList.set(groups.getSelectionModel().getSelectedIndex(),groupSelected.getText());
    }


    /**
     * Delete group.
     *
     * @param event the event
     */
    @FXML
    public void deleteGroup(ActionEvent event){
        boolean response = Dialogs.showConfirmationDialog("Are you sure you want to delete this group ?");
        if(response) {
            String selectedGrp = groups.getSelectionModel().getSelectedItem().toString();
            for (Group g : listGroups) {
                if (g.getNom().equals(selectedGrp)) {
                    //application.getIHMtoDATA().deleteGroup(g);
                    stub.deleteGroup(g);
                    obsGroupsList.remove(groups.getSelectionModel().getSelectedIndex());
                    groupSelected.clear();
                    disableFields(true);
                    deleteMemberBtn.setDisable(true);
                    return;
                }
            }
            obsMembersList.clear();
            members.setItems(obsMembersList);
        }
    }

    @FXML
    public void deleteMemberFromGroup(ActionEvent event){
        boolean response = Dialogs.showConfirmationDialog("Are you sure you want to delete this member ?");
        if(response) {
            String selectedGrp = groups.getSelectionModel().getSelectedItem().toString();
            String selectedMmb = members.getSelectionModel().getSelectedItem().toString();
            for (Group g : listGroups) {
                if (g.getNom().equals(selectedGrp)) {
                    for (User u : g.getUsers()) {
                        if((u.getFirstname()+" "+u.getLastname()).equals(selectedMmb)) {
                            //application.getIHMtoDATA().deleteUserFromGroup(u,g);
                            stub.deleteUserFromGroup(u,g);
                            obsMembersList.remove(members.getSelectionModel().getSelectedIndex());
                            deleteMemberBtn.setDisable(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * Adds the new group.
     *
     * @param event the event
     */
    @FXML
    public void addNewGroup(ActionEvent event){
        if(newGroupName.getText().equals("")) {
            showWarningDialog("Please enter a group name.");
        }
        else {
            Boolean exists = false;
            for (Group g : listGroups) {
                if (g.getNom().toLowerCase().equals(newGroupName.getText().toLowerCase())) {
                    exists = true;
                }
            }
            if (!exists) {
                //application.getIHMtoDATA().addGroup(new Group(newGroupName.getText()));
                stub.addGroup(new Group(newGroupName.getText()));
                obsGroupsList.add(newGroupName.getText());
                newGroupName.clear();
            } else {
                showErrorDialog("Group name already in use !");
            }
        }
    }

    @FXML
    public void addMemberInGroup(ActionEvent event) {
        if(addUserName.getText().equals("")) {
            showWarningDialog("Please the user's login.");
        }
        else {
            String selectedGrp = groups.getSelectionModel().getSelectedItem().toString();
            for (Group g : listGroups) {
                if (g.getNom().equals(selectedGrp)) {
                    Boolean inGroup = false;
                    for (User u : g.getUsers()) {
                        if (addUserName.getText().toLowerCase().equals(u.getLogin().toLowerCase())) {
                            showWarningDialog("This user is already in the group !");
                            inGroup = true;
                            return;
                        }
                    }

                    if(!inGroup) {
                        for (User u : g.getUsers()) {
                            User user = checkIfFriend(addUserName.getText().toLowerCase());
                            if(user != null){
                                //application.getIHMtoDATA().addUserInGroup(user,g);
                                stub.addUserInGroup(user,g);
                                obsMembersList.add(user.getFirstname()+ " " + user.getLastname());
                                addUserName.clear();
                                showInformationDialog(user.getFirstname()+ " " + user.getLastname() + " added to the group successfully.");
                                addUserName.clear();
                                return;
                            }
                            else {
                                showWarningDialog("This user is not in your friends list.");
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Display users.
     *
     * @param groupName the group name
     */
    private void displayUsers(String groupName) {
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
    private void disableFields (Boolean b) {
        groupSelected.setDisable(b);
        deleteGroupBtn.setDisable(b);
        addUserName.setDisable(b);
        addUserBtn.setDisable(b);
        members.setDisable(b);
    }

    @FXML
    private void finish() {
        ((Stage) gestionGroupes.getScene().getWindow()).close();
    }

    private User checkIfFriend(String login) {
        User user = null;
        for(Group g : listGroups) {
            for(User u : g.getUsers()) {
                if(u.getLogin().toLowerCase().equals(addUserName.getText().toLowerCase())) {
                    user = u;
                    return user;
                }
            }
        }
        return user;
    }
}
