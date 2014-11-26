package IHM.controllers;

import DATA.model.Group;
import DATA.model.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.UUID;

public class FriendsSubController extends SplitPane implements Initializable {

    private MainController application;

    @FXML
    private Accordion groupsAccordion;

    @FXML
    private Button btnAddFriend;

    @FXML
    private HBox boxAddFriend;

    @FXML
    private TextField friendName;

    private Map<String, List<UserHBoxCell>> groups;

    private class UserHBoxCell extends HBox {
        private User user;
        private Label label;
        private ImageView icon;

        private static final String onPath = "IHM/resources/online_icon.png";
        private static final String offPath = "IHM/resources/offline_icon.png";

        public UserHBoxCell(User user, boolean status) {
            this.user = user;

            label = new Label();
            label.setText(user.toString());
            HBox.setHgrow(label, Priority.ALWAYS);

            icon = new ImageView();
            icon.setImage(new Image(status ? onPath : offPath));
            icon.setFitHeight(11.0);
            icon.setFitWidth(13.0);

            this.getChildren().addAll(icon, label);
        }

        public void switchOn() {
            icon.setImage(new Image(onPath));
        }

        public void switchOff() {
            icon.setImage(new Image(offPath));
        }

        public User getUser() {
            return user;
        }
    }

    public FriendsSubController() {
        super();
    }

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        groups = Maps.newHashMap();
        boxAddFriend.setPadding(new Insets(15, 12, 15, 12));
    }

    /**
     * Build the GUI
     */
    public void build() {
        // Initialize groups
        List<Group> userGroups = application.getIHMtoDATA().getAllUsers();
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
            addUsersInGroup(users, groupName);
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
        List<UserHBoxCell> userGroup = groups.get(groupName);
        userGroup.add(new UserHBoxCell(user, user.isConnected()));
    }

    public void addUsersInGroup(final List<User> users, final String groupName) {
        if(users == null) {
            return;
        }
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

        List<UserHBoxCell> users = Lists.newArrayList();
        ListView<UserHBoxCell> listView = new ListView<UserHBoxCell>();
        ObservableList<UserHBoxCell> myObservableList = FXCollections.observableList(users);
        listView.setItems(myObservableList);
        listView.setEditable(true);

        tp.setContent(listView);
        groups.put(groupName, users);

        groupsAccordion.getPanes().add(tp);
        return tp;
    }

    public void addFriend() {
        String text = friendName.getText();
        application.getIHMtoDATA().addUserInGroup();
        friendName.clear();
    }

    public void setApp(final MainController app) {
        //TODO Treat pending friend requests
        this.application = app;
    }

    public void connectUser(UUID userId, String login) {
        UserHBoxCell user = lookForUser(userId);
        if(user != null) {
            user.switchOn();
        } else {
            addUserInGroup(Groups.DEFAULT_GROUP_NAME);
        }
    }

    public void disconnectUser(UUID userId, String login) {
        UserHBoxCell user = lookForUser(userId);
        if(user != null) {
            user.switchOff();
        }
    }

    public void receiveFriendRequest(User user) {
        Dialogs.DialogResponse ok = Dialogs.showConfirmDialog(application.getPrimaryStage(), user.toString() + " wants to be your friend ! Do you accept it ? ");
        if(ok.equals("YES")) {
            addUserInGroup(Groups.FRIENDS_GROUP_NAME);
            application.getIHMtoDATA().confirmUserInGroup();
        }
    }

    private UserHBoxCell lookForUser(UUID userId) {
        for(Entry<String, List<UserHBoxCell>> entry : groups.entrySet()) {
            List<UserHBoxCell> users = entry.getValue();
            for(UserHBoxCell u : users) {
                if(u.getUser().getUid().equals(userId)) {
                    return u;
                }
            }
        }
        return null;
    }

    private UserHBoxCell lookForUser(String firstLastName) {
        for(Entry<String, List<UserHBoxCell>> entry : groups.entrySet()) {
            List<UserHBoxCell> users = entry.getValue();
            for(UserHBoxCell u : users) {
                if(u.toString().equals(firstLastName)) {
                    return u;
                }
            }
        }
        return null;
    }
}
