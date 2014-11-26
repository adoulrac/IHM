package IHM.interfaces;

import DATA.model.Picture;
import DATA.model.User;
import IHM.controllers.*;
import javafx.scene.Parent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by greyna on 19/11/2014.
 */
public class DATAtoIHMimpl implements DATAtoIHM {

    private MainController app;

    private List<User> friendRequests = new ArrayList<User>();
    List<User> emptyPendingFriendRequests() {
        List<User> result = new ArrayList<User>(friendRequests);
        friendRequests = new ArrayList<User>();
        return result;
    }

    private List<UUID> connectedUsers = new ArrayList<UUID>();
    public List<UUID> getConnectedUsers() {
        return connectedUsers;
    }

    public DATAtoIHMimpl(MainController app) {
        this.app = app;
    }

    @Override
    public void receiveUser(User user, int queryId) {
        Parent controller = app.getRequests().get(queryId);
        if (controller==null){
            //no entry or it's deleted in the map
            if (app.getCurrentController() instanceof WelcomeController) {
                FriendsSubController c = ((WelcomeController) app.getCurrentController()).getFriendsSubController();
                c.addUser(user);
            } else {
                // do nothing (will be fetched later with sync method IHMtoDATA.getAllUsers)
            }
        }
        else {
            if (controller instanceof FriendsSubController) {
                ((FriendsSubController) controller).addUser(user);
            }
        }
    }

    @Override
    public void receiveConnectedUser(UUID userId, String login, int queryId) {
        Parent controller = app.getRequests().get(queryId);
        if (controller==null){
            //no entry or it's deleted in the map
            if (app.getCurrentController() instanceof WelcomeController) {
                FriendsSubController c = ((WelcomeController) app.getCurrentController()).getFriendsSubController();
                c.connectUser(userId, login);
            } else {
                // do nothing (will be fetched later with sync method this.getConnectedUsers)
            }
        }
        else {
            if (controller instanceof FriendsSubController) {
                ((FriendsSubController) controller).connectUser(userId, login);
            }
        }

        for(UUID id : connectedUsers)
            if (id==userId) return;
        connectedUsers.add(userId);
    }

    @Override
    public void receiveUnconnectedUser(UUID userId, String login, int queryId) {
        Parent controller = app.getRequests().get(queryId);
        if (controller==null){
            //no entry or it's deleted in the map
            if (app.getCurrentController() instanceof WelcomeController) {
                FriendsSubController c = ((WelcomeController) app.getCurrentController()).getFriendsSubController();
                c.disconnectUser(userId, login);
            } else {
                // do nothing (will be fetched later with sync method this.getConnectedUsers)
            }
        }
        else {
            if (controller instanceof FriendsSubController) {
                ((FriendsSubController) controller).disconnectUser(userId, login);
            }
        }

        connectedUsers.remove(userId);
    }

    @Override
    public void receivePicture(Picture picture, int queryId) {
        Parent controller = app.getRequests().get(queryId);
        if (controller==null){
            //no entry or it's deleted in the map
            // do nothing (we don't know for what these pictures are for)
        }
        else {
            if (controller instanceof TabbedPicturesSubController) {
                ((TabbedPicturesSubController) controller).addPicture(picture);
            } else if (controller instanceof PictureController) {
                ((PictureController) controller).addPicture(picture);
            }
        }
    }

    @Override
    public void receivePictures(List<Picture> pictures, int queryId) {
        Parent controller = app.getRequests().get(queryId);
        if (controller==null){
            //no entry or it's deleted in the map
            // do nothing (we don't know for what these pictures are for)
        }
        else {
            if (controller instanceof TabbedPicturesSubController) {
                ((TabbedPicturesSubController) controller).addPictures(pictures);
            } else if (controller instanceof PictureController) {
                ((PictureController) controller).addPictures(pictures);
            }
        }
    }

    @Override
    public void receiveFriendRequest(User user, int queryId) {
        Parent controller = app.getRequests().get(queryId);
        if (controller==null){
            //no entry or it's deleted in the map
            if (app.getCurrentController() instanceof WelcomeController) {
                FriendsSubController c = ((WelcomeController) app.getCurrentController()).getFriendsSubController();
                c.receiveFriendRequest(user);
            } else {
                // store friend request to be fetched later with this.emptyPendingFriendRequests
                friendRequests.add(user);
            }
        }
        else {
            if (controller instanceof FriendsSubController) {
                // Confirmation of our friend request
                ((FriendsSubController) controller).receiveFriendRequest(user);
            }
        }
    }
}
