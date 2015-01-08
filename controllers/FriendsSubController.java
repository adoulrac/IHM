package IHM.controllers;

import DATA.model.Group;
import DATA.model.User;
import IHM.utils.Dialogs;
import IHM.utils.StringUtil;
import com.google.common.base.Strings;
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
import java.util.logging.Level;
import java.util.logging.Logger;

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

        /** The Constant ON_PATH. */
        private static final String ON_PATH = "IHM/resources/online_icon.png";

        /** The Constant OFF_PATH. */
        private static final String OFF_PATH = "IHM/resources/offline_icon.png";

        /**
         * Instantiates a new user h box cell.
         *
         * @param vUser the user
         * @param status the status
         */
        public UserHBoxCell(final User vUser, final boolean status) {
            this.user = vUser;

            label = new Label();
            label.setText(user.getLogin());
            HBox.setHgrow(label, Priority.ALWAYS);

            icon = new ImageView();
            icon.setImage(new Image(status ? ON_PATH : OFF_PATH));
            final float iconHeight = 11.0f;
            final float iconWidth = 13.0f;
            icon.setFitHeight(iconHeight);
            icon.setFitWidth(iconWidth);

            this.getChildren().addAll(icon, label);

            this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(final MouseEvent mouseEvent) {
                    application.goToProfile(user);
                }
            });
        }

        /**
         * Switch on. Sets the connected user icon.
         */
        public void switchOn() {
            icon.setImage(new Image(ON_PATH));
        }

        /**
         * Switch off. Sets the disconnected user icon.
         */
        public void switchOff() {
            icon.setImage(new Image(OFF_PATH));
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
        final Insets insets = new Insets(15, 12, 15, 12);
        boxAddFriend.setPadding(insets);
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
        if (userGroup != null) {
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
        if (users == null) {
            return;
        }
        for (User user : users) {
            addUserInGroup(user, groupName);
        }
    }

    /**
     * Creates a new group.
     *
     * @param groupName the group name
     * @return the titled pane
     */
    private TitledPane createNewGroup(final String groupName) {
        TitledPane tp = new TitledPane();
        final float width = 200.0f;
        final float height = 180.0f;
        tp.setPrefWidth(width);
        tp.setPrefHeight(height);
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
     * Adds a friend.
     */
    public void addFriend() {
        String friend = friendName.getText();
        UserHBoxCell userToAdd = lookForUser(friend);
        if(userToAdd != null) {
            if(!isMyFriend(userToAdd.getUser())) {
                application.getIHMtoDATA().addUserInGroup(userToAdd.getUser(), getFriendGroup());
                Dialogs.showInformationDialog("Une demande d'ami a été envoyé à " + friend);
            } else {
                Dialogs.showInformationDialog("Ajout impossible: l'utilisateur est déjà dans vos amis.");
            }
        } else {
            Dialogs.showInformationDialog("Utilisateur inconnu.");
        }
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
    public void connectUser(final User user) {
        User existingUser = lookForUser(user);
        if (existingUser != null) {
            reloadUser(user);
        } else {
            addUserInGroup(user, Group.DEFAULT_GROUP_NAME);
        }
    }

    /**
     * Disconnect user.
     *
     * @param user the user
     */
    public void disconnectUser(final User user) {
        User existingUser = lookForUser(user);
        if (existingUser != null) {
            reloadUser(user);
        }
    }

    /**
     * Receive friend response.
     *
     * @param sender the sender
     * @param response the response
     */
    public void receiveFriendResponse(final User sender, final boolean response) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
        if (response) {
            Dialogs.showInformationDialog("L'utilisateur " + sender.getLogin() + " a accepté votre demande d'amis.");
            application.getIHMtoDATA().addUserInGroup(sender, getFriendGroup());
            moveUserToGroup(sender, Group.FRIENDS_GROUP_NAME);
        } else {
            Dialogs.showInformationDialog("L'utilisateur " + sender.getLogin() + " a refusé votre demande d'amis.");
        }
        }});
    }

    /**
     * Receive friend request.
     *
     * @param sender the sender
     */
    public void receiveFriendRequest(final User sender) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boolean response = Dialogs.showConfirmationDialog(sender.getLogin()
                        + " veut être votre ami ! Acceptez-vous sa demande ? ");
                if (response) {
                    application.getIHMtoDATA().acceptUserInGroup(sender, getFriendGroup());
                    application.getIHMtoDATA().deleteUserFromGroup(sender, getDefaultGroup());
                    moveUserToGroup(sender, Group.FRIENDS_GROUP_NAME);
                } else {
                    application.getIHMtoDATA().refuseUser(sender);
                }
            }
        });
    }

    private Group getFriendGroup() {
        return application.getIHMtoDATA().getGroupByName(Group.FRIENDS_GROUP_NAME);
    }

    private Group getDefaultGroup() {
        return application.getIHMtoDATA().getGroupByName(Group.DEFAULT_GROUP_NAME);
    }

    /**
     * Look for user.
     *
     * @param user the user
     * @return the user h box cell
     */
    private User lookForUser(final User user) {
        for (Entry<String, ObservableList<UserHBoxCell>> entry : groups.entrySet()) {
            List<UserHBoxCell> users = entry.getValue();
            for (UserHBoxCell u : users) {
                if (u.getUser().getUid().equals(user.getUid())) {
                    Logger.getLogger(FriendsSubController.class.getName()).log(Level.INFO, "Looking for user in group list: User "+user.getLogin()+" found in group " + entry.getKey());
                    return u.getUser();
                }
            }
        }
        Logger.getLogger(FriendsSubController.class.getName()).log(Level.INFO, "Looking for user in group list: User "+user.getLogin()+" not found");
        return null;
    }

    private boolean isMyFriend(User user) {
        List<User> users = getFriendGroup().getUsers();
        for(User u : users) {
            if(u.getUid().equals(user.getUid())){
                return true;
            }
        }
        return false;
    }

    /**
     * Look for user.
     *
     * @param login the login
     * @return the user h box cell
     */
    private UserHBoxCell lookForUser(final String login) {
        if(Strings.isNullOrEmpty(login)) {
            return null;
        }

        for (Entry<String, ObservableList<UserHBoxCell>> entry : groups.entrySet()) {
            List<UserHBoxCell> users = entry.getValue();
            for (UserHBoxCell u : users) {
                if (u.getUser().getLogin().equalsIgnoreCase(login)) {
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
    private String removeUserFromGroup(final User user) {
        UserHBoxCell userToRemove = null;
        for (Entry<String, ObservableList<UserHBoxCell>> entry : groups.entrySet()) {
            List<UserHBoxCell> users = entry.getValue();
            for (UserHBoxCell u : users) {
                if (u.getUser().getUid().equals(user.getUid())) {
                    userToRemove = u;
                }
            }
            if (userToRemove != null) {
                users.remove(userToRemove);
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Move user.
     *
     * @param user the user
     * @param groupName the group name
     */
    private void moveUserToGroup(final User user, final String groupName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            if(user != null) {
                String currGroupName = removeUserFromGroup(user);
                if(currGroupName != null || groupName != null) {
                    addUserInGroup(user, groupName == null ? currGroupName : groupName);
                }
            }
            }
        });
    }

    private void reloadUser(final User user) {
        moveUserToGroup(user, null);
    }

}
