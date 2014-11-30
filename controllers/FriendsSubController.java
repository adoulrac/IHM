package IHM.controllers;

import DATA.model.Group;
import DATA.model.User;
import IHM.utils.Dialogs;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * The Class FriendsSubController.
 */
public class FriendsSubController extends SplitPane implements Initializable {

    /** The application. */
    private MainController application;

    /** The groups accordion. */
    @FXML
    private Accordion groupsAccordion;

    /** The btn add friend. */
    @FXML
    private Button btnAddFriend;

    /** The box add friend. */
    @FXML
    private HBox boxAddFriend;

    /** The friend name. */
    @FXML
    private TextField friendName;

    /** The groups. */
    private Map<String, ObservableList<UserHBoxCell>> groups;

    /**
     * The Class UserHBoxCell.
     */
    private class UserHBoxCell extends HBox {
        
        /** The user. */
        private User user;
        
        /** The label. */
        private Label label;
        
        /** The icon. */
        private ImageView icon;

        /** The Constant onPath. */
        private static final String onPath = "IHM/resources/online_icon.png";
        
        /** The Constant offPath. */
        private static final String offPath = "IHM/resources/offline_icon.png";

        /**
         * Instantiates a new user h box cell.
         *
         * @param user the user
         * @param status the status
         */
        public UserHBoxCell(final User user, boolean status) {
            this.user = user;

            label = new Label();
            label.setText(user.getLogin());
            HBox.setHgrow(label, Priority.ALWAYS);

            icon = new ImageView();
            icon.setImage(new Image(status ? onPath : offPath));
            icon.setFitHeight(11.0);
            icon.setFitWidth(13.0);

            this.getChildren().addAll(icon, label);

            this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    application.goToProfile(user);
                }
            });
        }

        /**
         * Switch on.
         */
        public void switchOn() {
            icon.setImage(new Image(onPath));
        }

        /**
         * Switch off.
         */
        public void switchOff() {
            icon.setImage(new Image(offPath));
        }

        /**
         * Gets the user.
         *
         * @return the user
         */
        public User getUser() {
            return user;
        }
    }

    /**
     * Instantiates a new friends sub controller.
     */
    public FriendsSubController() {
        super();
    }

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        groups = Maps.newHashMap();
        boxAddFriend.setPadding(new Insets(15, 12, 15, 12));
    }

    /**
     * Build the GUI.
     */
    public void build() {
        // Initialize groups
        List<Group> userGroups = application.getIHMtoDATA().getAllUsers();
        addGroups(userGroups);
    }

    /**
     * Add a group with its users to the GUI.
     *
     * @param group the group
     */
    public void addGroup(final Group group) {
        if (group == null) {
            return;
        }
        String groupName = group.getNom();
        createNewGroup(groupName);

        List<User> users = group.getUsers();
        if (users != null) {
            addUsersInGroup(users, groupName);
        }
    }

    /**
     * Adds the groups.
     *
     * @param groupsParam the groups param
     */
    public void addGroups(final List<Group> groupsParam) {
        if (groupsParam == null) {
            return;
        }
        for (Group group : groupsParam) {
            addGroup(group);
        }
    }

    /**
     * Add a user in an existing group.
     *
     * @param user the user
     * @param groupName the group name
     */
    public void addUserInGroup(final User user, final String groupName) {
        final List<UserHBoxCell> userGroup = groups.get(groupName);
        if(userGroup != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    userGroup.add(new UserHBoxCell(user, user.isConnected()));
                }
            });
        }
    }

    /**
     * Adds the users in group.
     *
     * @param users the users
     * @param groupName the group name
     */
    public void addUsersInGroup(final List<User> users, final String groupName) {
        if(users == null) {
            return;
        }
        for (User user : users) {
            addUserInGroup(user, groupName);
        }
    }

    /**
     * Creates the new group.
     *
     * @param groupName the group name
     * @return the titled pane
     */
    private TitledPane createNewGroup(final String groupName) {
        TitledPane tp = new TitledPane();
        tp.setPrefWidth(200.0);
        tp.setPrefHeight(180.0);
        tp.setAnimated(false);
        tp.setTextFill(Paint.valueOf("WHITE"));
        tp.setText(groupName);

        List<UserHBoxCell> users = Lists.newArrayList();
        ListView<UserHBoxCell> listView = new ListView<UserHBoxCell>();
        ObservableList<UserHBoxCell> myObservableList = FXCollections.observableList(users);
        listView.setItems(myObservableList);
        listView.setEditable(true);

        tp.setContent(listView);
        groups.put(groupName, myObservableList);

        groupsAccordion.getPanes().add(tp);
        return tp;
    }

    /**
     * Adds the friend.
     */
    public void addFriend() {
        String friend = friendName.getText();
        UserHBoxCell userToAdd = lookForUser(friend);
        application.getIHMtoDATA().addUserInGroup(userToAdd.getUser(), application.getIHMtoDATA().getGroups().get(0));
        Dialogs.showInformationDialog("A friend request has been sent to " + friend);
        friendName.clear();
    }

    /**
     * Sets the app.
     *
     * @param app the new app
     */
    public void setApp(final MainController app) {
        this.application = app;
    }

    /**
     * Connect user.
     *
     * @param user the user
     */
    public void connectUser(User user) {
        UserHBoxCell existingUser = lookForUser(user.getUid());
        if(existingUser != null) {
            updateUser(user);
        } else {
            addUserInGroup(user, Group.DEFAULT_GROUP_NAME);
        }
    }

    /**
     * Disconnect user.
     *
     * @param user the user
     */
    public void disconnectUser(User user) {
        UserHBoxCell existingUser = lookForUser(user.getUid());
        if(existingUser != null) {
            updateUser(user);
        }
    }

    /**
     * Receive friend response.
     *
     * @param sender the sender
     * @param response the response
     */
    public void receiveFriendResponse(User sender, boolean response) {
        if (response) {
            Dialogs.showInformationDialog("The user " + sender.getLogin() + " has accepted your friend request.");
            updateUser(sender, Group.FRIENDS_GROUP_NAME);
        } else {
            Dialogs.showInformationDialog("The user " + sender.getLogin() + " has refused your friend request.");
        }
    }

    /**
     * Receive friend request.
     *
     * @param sender the sender
     */
    public void receiveFriendRequest(User sender) {
        boolean response = Dialogs.showConfirmationDialog(sender.getLogin() + " wants to be your friend ! Do you accept it ? ");
        if (response) {
            updateUser(sender, Group.FRIENDS_GROUP_NAME);
            application.getIHMtoDATA().acceptUserInGroup(sender, application.getIHMtoDATA().getGroups().get(0));
        }else {
            application.getIHMtoDATA().refuseUser(sender);
        }
    }

    /**
     * Look for user.
     *
     * @param userId the user id
     * @return the user h box cell
     */
    private UserHBoxCell lookForUser(UUID userId) {
        for(Entry<String, ObservableList<UserHBoxCell>> entry : groups.entrySet()) {
            List<UserHBoxCell> users = entry.getValue();
            for(UserHBoxCell u : users) {
                if(u.getUser().getUid().equals(userId)) {
                    return u;
                }
            }
        }
        return null;
    }

    /**
     * Look for user.
     *
     * @param login the login
     * @return the user h box cell
     */
    private UserHBoxCell lookForUser(String login) {
        for(Entry<String, ObservableList<UserHBoxCell>> entry : groups.entrySet()) {
            List<UserHBoxCell> users = entry.getValue();
            for(UserHBoxCell u : users) {
                if(u.getUser().getLogin().equals(login)) {
                    return u;
                }
            }
        }
        return null;
    }

    /**
     * Removes the user in group.
     *
     * @param user the user
     * @return the string
     */
    private String removeUserInGroup(User user) {
        UserHBoxCell userToRemove = null;
        for(Entry<String, ObservableList<UserHBoxCell>> entry : groups.entrySet()) {
            List<UserHBoxCell> users = entry.getValue();
            for (UserHBoxCell u : users) {
                if(u.getUser().getUid().equals(user.getUid())) {
                    userToRemove = u;
                }
            }
            if(userToRemove != null){
                users.remove(userToRemove);
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Update user.
     *
     * @param user the user
     * @param groupName the group name
     */
    private void updateUser(final User user, final String groupName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String CurrGroupName = removeUserInGroup(user);
                addUserInGroup(user, groupName == null ? CurrGroupName : groupName);
            }
        });
    }

    /**
     * Update user.
     *
     * @param user the user
     */
    private void updateUser(final User user) {
        updateUser(user, null);
    }
}
