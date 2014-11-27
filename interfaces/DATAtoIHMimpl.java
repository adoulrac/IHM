package IHM.interfaces;

import DATA.model.Picture;
import DATA.model.User;
import IHM.controllers.*;
import com.google.common.collect.Lists;
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

    private List<User> friendRequests = Lists.newArrayList();

    public List<User> emptyPendingFriendRequests() {
        List<User> result = new ArrayList<User>(friendRequests);
        friendRequests = new ArrayList<User>();
        return result;
    }

    private List<UUID> connectedUsers = new ArrayList<UUID>();

    @Override
    public void receiveConnectedUser(User user) {
        if(app.getWelcomeController() != null)
            app.getWelcomeController().getFriendsSubController().connectUser(user);
    }

    @Override
    public void receiveUnconnectedUser(User user) {
        if (app.getWelcomeController() != null)
            app.getWelcomeController().getFriendsSubController().disconnectUser(user);
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
                app.removeRequest(queryId);
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
                app.removeRequest(queryId);
            }
        }
    }

    @Override
    public void receiveFriendRequest(User user, Integer queryId) {
        if(queryId == null) {
            // Default behavior on welcome page
            app.getWelcomeController().getFriendsSubController().receiveFriendRequest(user);
        } else {
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
                    app.removeRequest(queryId);
                }
            }
        }
    }

    public List<UUID> getConnectedUsers() {
        return connectedUsers;
    }

    public DATAtoIHMimpl(MainController app) {
        this.app = app;
    }
}
