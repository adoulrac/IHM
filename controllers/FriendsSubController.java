package IHM.controllers;

import DATA.model.Group;
import DATA.model.User;
import IHM.Tester;
import com.google.common.collect.Maps;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

public class FriendsSubController extends SplitPane implements Initializable {

    private static final String DEFAULT_GROUP_NAME = "Tous les utilisateurs";

    private MainController application;

    @FXML
    private Accordion groupsAccordion;

    @FXML
    private Button btnAddFriend;

    @FXML
    private TextField friendName;

    private Map<String, ObservableList<User>> groups;

    public FriendsSubController() {
        super();
    }

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        groups = Maps.newHashMap();
    }

    /**
     * Build the GUI
     */
    public void build() {
        //Build the default users section
        createNewGroup(DEFAULT_GROUP_NAME);

        // Call Data to get local groups
        List<Group> userGroups = application.getIHMtoDATA().getGroups();
        addGroups(userGroups);
    }

    /**
     * Add a group with its users to the GUI
     * @param group
     */
    public void addGroup(final Group group) {
        if (group == null) {
            return;
        }
        String groupName = group.getNom();
        createNewGroup(groupName);

        List<User> users = group.getUsers();
        if (users != null) {
            for (User user : users)
            {
                addUserInGroup( user, groupName );
            }
        }
    }

    public void addGroups(final List<Group> groupsParam) {
        if (groupsParam == null) {
            return;
        }
        for (Group group : groupsParam) {
            addGroup(group);
        }
    }

    /**
     * Add a user in an existing group
     */
    public void addUserInGroup(final User user, final String groupName) {
        ObservableList<User> userGroup = groups.get(groupName);
        userGroup.add(user);
    }

    public void addUsersInGroup(final List<User> users, final String groupName) {
        for (User user : users) {
            addUserInGroup(user, groupName);
        }
    }

    private TitledPane createNewGroup(final String groupName) {
        TitledPane tp = new TitledPane();
        tp.setPrefWidth(200.0);
        tp.setPrefHeight(180.0);
        tp.setAnimated(false);
        tp.setTextFill(Paint.valueOf("WHITE"));
        tp.setText(groupName);

        ObservableList<User> items = FXCollections.observableArrayList();
        ListView users = new ListView();
        users.setEditable(true);
        users.setItems(items);

        tp.setContent(users);
        groups.put(groupName, items);

        groupsAccordion.getPanes().add(tp);
        return tp;
    }

    public void addFriend() {
        String text = friendName.getText();
        //TODO: send the request to the selected user
        friendName.clear();
    }

    public void setApp(final MainController app) {
        //TODO treat pending friend requests from List<User> returned by app.getDATAInterfaceReceiver().emptyPendingFriendRequests()
        this.application = app;
    }

    public void addUser(User user) {
        if(user == null) {
            return;
        }
        addUserInGroup(user, DEFAULT_GROUP_NAME);
    }

    public void connectUser(UUID userId, String login) {
        //TODO update user state with this connection notification received in async
    }

    public void disconnectUser(UUID userId, String login) {
        //TODO update user state with this disconnection notification received in async
    }

    public void receiveFriendRequest(User user) {
        //TODO receive friend request (could be a confirmation of pending request OR new request from other user)
    }
}
