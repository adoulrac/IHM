package IHM.interfaces;

import DATA.model.Picture;
import DATA.model.User;
import IHM.controllers.FriendsSubController;
import IHM.controllers.MainController;
import IHM.controllers.WelcomeController;
import javafx.scene.Parent;

import java.util.ArrayList;
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

    public DATAtoIHMimpl(MainController app) {
        this.app = app;
    }

    @Override
    public void receiveConnectedUser(User user, int queryId) {
        Parent controller = app.getRequests().get(queryId);
        if(queryId == -1 || controller!=null) {
            if (controller instanceof FriendsSubController) {
                ((FriendsSubController) controller).connectUser(user);
            }
        }else {
            //no entry or it's deleted in the map
            //TODO decide what happens
        }
    }

    @Override
    public void receiveUnconnectedUser(User user, int queryId) {
        Parent controller = app.getRequests().get(queryId);
        if(queryId == -1 || controller!=null) {
            if (controller instanceof FriendsSubController) {
                ((FriendsSubController) controller).disconnectUser(user);
            }
        }else {
            //no entry or it's deleted in the map
            //TODO decide what happens
        }
    }

    @Override
    public void receivePicture(Picture picture, int queryId) {

    }

    @Override
    public void receivePictures(List<Picture> pictures, int queryId) {

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
                // store friend request
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
