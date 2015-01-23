package IHM.controllers;

import DATA.model.Group;
import DATA.model.User;
import IHM.utils.Dialogs;
import com.google.common.base.Strings;
import javafx.collections.FXCollections;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class GroupsController.
 */
public class GroupsController implements Initializable {

    /**
     * The obs groups list.
     */
    private final ObservableList
            obsGroupsList = FXCollections.observableArrayList();
    /**
     * The obs members list.
     */
    private final ObservableList
            obsMembersList = FXCollections.observableArrayList();

    /**
     * The groups list.
     */
    private List<Group> listGroups;

    /**
     * The groups pane.
     */
    @FXML
    private TitledPane manageGroups;

    /**
     * The groups listview.
     */
    @FXML
    private ListView groups;
    /**
     * The members.
     */
    @FXML
    private ListView members;
    /**
     * The group selected.
     */
    @FXML
    private TextField groupSelected;
    /**
     * The new group name.
     */
    @FXML
    private TextField newGroupName;
    /**
     * The add user name.
     */
    @FXML
    private TextField addUserName;
    /**
     * The add user btn.
     */
    @FXML
    private Button addUserBtn;
    /**
     * The delete group btn.
     */
    @FXML
    private Button deleteGroupBtn;

    /**
     * The delete member button.
     */
    @FXML
    private Button deleteMemberBtn;
    /**
     * The application.
     */
    private MainController application;

    /**
     * Sets the app.
     *
     * @param app the new app
     */
    public final void setApp(final MainController app) {
        this.application = app;
    }

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize
     * (java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        newGroupName.setPromptText("Ajouter groupe...");
        addUserName.setPromptText("Ajouter membre...");
        listGroups = new ArrayList<Group>();
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
            Logger.getLogger(GroupsController.class.getName())
                    .log(Level.SEVERE, e.getMessage());
        }

        groups.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
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
                } catch (Exception e) {
                    Logger.getLogger(GroupsController.class.getName())
                            .log(Level.SEVERE, e.getMessage());
                }
            }
        });

        members.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                try {
                    if (members.getSelectionModel().getSelectedItem() != null) {
                        deleteMemberBtn.setDisable(false);
                    }
                } catch (Exception e) {
                    Logger.getLogger(GroupsController.class.getName())
                            .log(Level.SEVERE, e.getMessage());
                }
            }
        });

        groups.setItems(obsGroupsList);

        if (listGroups != null) {
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
    public void changeGroupName(final ActionEvent event) {
        application.getIHMtoDATA().addGroup(new Group(groupSelected.getText()));
        obsGroupsList.set(groups.getSelectionModel().getSelectedIndex(),
                groupSelected.getText());
    }


    /**
     * Delete group.
     *
     * @param event the event
     */
    @FXML
    public void deleteGroup(final ActionEvent event) {
        String groupToDelete =
                groups.getSelectionModel().getSelectedItem().toString();
        if (groupToDelete.equals(Group.FRIENDS_GROUP_NAME)) {
            Dialogs.showInformationDialog("Suppression impossible,"
                    + " le groupe Amis ne peut pas être supprimé.");
            return;
        }
        boolean response = Dialogs.showConfirmationDialog("Confirmez-vous"
                + " la suppression du groupe ?");
        if (response) {
            String selectedGrp =
                    groups.getSelectionModel().getSelectedItem().toString();
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

    /**
     * Delete a member from a group.
     *
     * @param event the event
     */
    @FXML
    public void deleteMemberFromGroup(final ActionEvent event) {
        boolean response =
                Dialogs.showConfirmationDialog("Confirmez-vous la suppression du membre ?");
        if (response) {
            String selectedGrp =
                    groups.getSelectionModel().getSelectedItem().toString();
            String selectedMmb =
                    members.getSelectionModel().getSelectedItem().toString();
            for (Group g : listGroups) {
                if (g.getNom().equals(selectedGrp)) {
                    for (User u : g.getUsers()) {
                        if ((u.getLogin()).equals(selectedMmb)) {
                            application.getIHMtoDATA().deleteUserFromGroup(u, g);
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
    public void addNewGroup(final ActionEvent event) {
        if (Strings.isNullOrEmpty(newGroupName.getText())) {
            Dialogs.showWarningDialog("Veuillez entrer un nom de groupe.");
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
                Dialogs.showErrorDialog("Le groupe existe déjà !");
            }
        }
    }

    /**
     * Add a member in a group.
     *
     * @param event the event
     */
    @FXML
    public void addMemberInGroup(final ActionEvent event) {
        if (Strings.isNullOrEmpty(addUserName.getText())) {
            Dialogs.showWarningDialog("Veuillez"
                    + " entrer le login de l'utilisateur.");
        } else {
            String selectedGrp =
                    groups.getSelectionModel().getSelectedItem().toString();
            for (Group g : listGroups) {
                if (g.getNom().equals(selectedGrp)) {
                    Boolean inGroup = false;
                    for (User u : g.getUsers()) {
                        if (addUserName.getText().equalsIgnoreCase(u.getLogin())) {
                            Dialogs.showWarningDialog("Ajout impossible: L'utilisateur existe déjà dans le groupe !");
                            inGroup = true;
                            return;
                        }
                    }

                    if (!inGroup) {
                        User user = checkIfFriend(addUserName.getText());
                        if (user != null) {
                            application.getIHMtoDATA().addUserInGroup(user, g);
                            obsMembersList.add(user.getLogin());
                            Dialogs.showInformationDialog(user.getLogin() + " a été ajouté dans le groupe avec succès.");
                            addUserName.clear();
                            return;
                        } else {
                            Dialogs.showWarningDialog("Ajout impossible: L'utilisateur n'est pas votre ami ou n'existe pas.");
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
    private void displayUsers(final String groupName) {
        if (groupName == null) {
            obsMembersList.clear();
        } else {
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
    private void disableFields(final boolean b) {
        groupSelected.setDisable(b);
        deleteGroupBtn.setDisable(b);
        addUserName.setDisable(b);
        addUserBtn.setDisable(b);
        members.setDisable(b);
    }

    /**
     * Finish the rules modifications.
     *
     */
    @FXML
    private void finish() {
        ((Stage) manageGroups.getScene().getWindow()).close();
    }

    /**
     * Checks if a user referenced by its login is a friend.
     *@param login the tested user login.
     *@return the user if it is a friend, else null.
     */
    private User checkIfFriend(final String login) {
        User user = null;
        for (Group g : listGroups) {
            for (User u : g.getUsers()) {
                if (u.getLogin().equalsIgnoreCase(addUserName.getText())) {
                    user = u;
                    return user;
                }
            }
        }
        return user;
    }
}
