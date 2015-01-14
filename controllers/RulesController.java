package IHM.controllers;

import DATA.model.Group;
import DATA.model.User;
import IHM.utils.Dialogs;
import com.google.common.base.Strings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static IHM.utils.Dialogs.*;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * The Class GroupsController.
 */
public class RulesController implements Initializable {
    
    /** The rules. */
    @FXML
    private TitledPane manageRules;

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
    }

    /**
     * Load groups.
     */
    public void loadGroups() {
        disableFields(true);
        deleteMemberBtn.setDisable(true);
        try {
            listGroups = application.getIHMtoDATA().getGroups();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        groups.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            try {
                Object item = groups.getSelectionModel().getSelectedItem();
                if (item != null) {
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
                if (members.getSelectionModel().getSelectedItem() != null) {
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
        application.getIHMtoDATA().addGroup(new Group(groupSelected.getText()));
        obsGroupsList.set(groups.getSelectionModel().getSelectedIndex(),groupSelected.getText());
    }


    /**
     * Delete group.
     *
     * @param event the event
     */
    @FXML
    public void deleteGroup(ActionEvent event){
        String groupToDelete = groups.getSelectionModel().getSelectedItem().toString();
        if(groupToDelete.equals(Group.FRIENDS_GROUP_NAME)) {
            Dialogs.showInformationDialog("Suppression impossible, le groupe Amis ne peut pas être supprimé.");
            return;
        }
        boolean response = Dialogs.showConfirmationDialog("Confirmez-vous la suppression du groupe ?");
        if(response) {
            String selectedGrp = groups.getSelectionModel().getSelectedItem().toString();
            for (Group g : listGroups) {
                if (g.getNom().equals(selectedGrp)) {
                    application.getIHMtoDATA().deleteGroup(g);
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
        boolean response = Dialogs.showConfirmationDialog("Confirmez-vous la suppression du membre ?");
        if(response) {
            String selectedGrp = groups.getSelectionModel().getSelectedItem().toString();
            String selectedMmb = members.getSelectionModel().getSelectedItem().toString();
            for (Group g : listGroups) {
                if (g.getNom().equals(selectedGrp)) {
                    for (User u : g.getUsers()) {
                        if((u.getLogin()).equals(selectedMmb)) {
                            application.getIHMtoDATA().deleteUserFromGroup(u,g);
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
        if(Strings.isNullOrEmpty(newGroupName.getText())) {
            showWarningDialog("Veuillez entrer un nom de groupe.");
        } else {
            Boolean exists = false;
            for (Group g : listGroups) {
                if (g.getNom().equalsIgnoreCase(newGroupName.getText())) {
                    exists = true;
                }
            }
            if (!exists) {
                application.getIHMtoDATA().addGroup(new Group(newGroupName.getText()));
                obsGroupsList.add(newGroupName.getText());
                newGroupName.clear();
            } else {
                showErrorDialog("Le groupe existe déjà !");
            }
        }
    }

    @FXML
    public void addMemberInGroup(ActionEvent event) {
        if(Strings.isNullOrEmpty(addUserName.getText())) {
            showWarningDialog("Veuillez entrer le login de l'utilisateur.");
        }
        else {
            String selectedGrp = groups.getSelectionModel().getSelectedItem().toString();
            for (Group g : listGroups) {
                if (g.getNom().equals(selectedGrp)) {
                    Boolean inGroup = false;
                    for (User u : g.getUsers()) {
                        if (addUserName.getText().equalsIgnoreCase(u.getLogin())) {
                            showWarningDialog("Ajout impossible: L'utilisateur existe déjà dans le groupe !");
                            inGroup = true;
                            return;
                        }
                    }

                    if(!inGroup) {
                        User user = checkIfFriend(addUserName.getText());
                        if(user != null){
                            application.getIHMtoDATA().addUserInGroup(user, g);
                            obsMembersList.add(user.getLogin());
                            showInformationDialog(user.getLogin() + " a été ajouté dans le groupe avec succès.");
                            addUserName.clear();
                            return;
                        } else {
                            showWarningDialog("Ajout impossible: L'utilisateur n'est pas votre ami ou n'existe pas.");
                            return;
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
        if(groupName == null) {
            obsMembersList.clear();
        }
        else {
            obsMembersList.clear();
            for (Group g : listGroups) {
                if (g.getNom().equals(groupName)) {
                    for (User u : g.getUsers()) {
                        obsMembersList.add(u.getLogin());
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
        ((Stage) manageRules.getScene().getWindow()).close();
    }

    private User checkIfFriend(String login) {
        User user = null;
        for(Group g : listGroups) {
            for(User u : g.getUsers()) {
                if(u.getLogin().equalsIgnoreCase(addUserName.getText())) {
                    user = u;
                    return user;
                }
            }
        }
        return user;
    }
}