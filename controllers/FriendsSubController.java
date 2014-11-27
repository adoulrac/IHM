package IHM.controllers;

import DATA.model.Group;
import DATA.model.User;
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

    private Map<String, ObservableList<UserHBoxCell>> groups;

    private class UserHBoxCell extends HBox {
        private User user;
        private Label label;
        private ImageView icon;

        private static final String onPath = "IHM/resources/online_icon.png";
        private static final String offPath = "IHM/resources/offline_icon.png";

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
        groups.put(groupName, myObservableList);

        groupsAccordion.getPanes().add(tp);
        return tp;
    }

    public void addFriend() {
        String text = friendName.getText();
        UserHBoxCell userToAdd = lookForUser(text);

        application.getIHMtoDATA().addUserInGroup(userToAdd.getUser(), application.getIHMtoDATA().getGroups().get(0));
        Dialogs.showInformationDialog(application.getPrimaryStage(), "A friend request has been sent to " + text);
        friendName.clear();
    }

    public void setApp(final MainController app) {
        this.application = app;
    }

    public void connectUser(User user) {
        UserHBoxCell existingUser = lookForUser(user.getUid());
        if(existingUser != null) {
            updateUser(user);
        } else {
            addUserInGroup(user, Group.DEFAULT_GROUP_NAME);
        }
    }

    public void disconnectUser(User user) {
        UserHBoxCell existingUser = lookForUser(user.getUid());
        if(existingUser != null) {
            updateUser(user);
        }
    }

    public void receiveFriendRequest(User user) {
        Dialogs.DialogResponse ok = Dialogs.showConfirmDialog(application.getPrimaryStage(), user.toString() + " wants to be your friend ! Do you accept it ? ");
        if(ok.equals("YES")) {
            updateUser(user, Group.FRIENDS_GROUP_NAME);
            application.getIHMtoDATA().acceptUserInGroup(user, application.getIHMtoDATA().getGroups().get(0));
        }
    }

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

    private void updateUser(final User user, final String groupName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String CurrGroupName = removeUserInGroup(user);
                addUserInGroup(user, groupName == null ? CurrGroupName : groupName);
            }
        });
    }

    private void updateUser(final User user) {
        updateUser(user, null);
    }
}
