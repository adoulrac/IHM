package IHM.interfaces;

import DATA.model.Picture;
import DATA.model.User;
import IHM.controllers.*;
import com.google.common.collect.Lists;
import javafx.fxml.Initializable;
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
        Initializable controller = app.getRequests().get(queryId);
        if (controller==null){
            //no entry or it's deleted in the map
            // do nothing (we don't know for what these pictures are for)
        }
        else {
            if (controller instanceof TabbedPicturesSubController) {
                ((TabbedPicturesSubController) controller).addPicture(picture);
                app.removeRequest(queryId);
            } else
            if (controller instanceof PictureController) {
                ((PictureController) controller).receiveFullImage(picture);
                app.removeRequest(queryId);
            }
        }
    }

    @Override
    public void receivePictures(List<Picture> pictures, int queryId) {
        Initializable controller = app.getRequests().get(queryId);
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
    public void receiveFriendRequest(User user) {
        WelcomeController welcome = app.getWelcomeController();
        if(welcome != null){
            welcome.getFriendsSubController().receiveFriendRequest(user);
        }
    }

    @Override
    public void receiveFriendResponse(User user, boolean response) {
        WelcomeController welcome = app.getWelcomeController();
        if(welcome != null){
            welcome.getFriendsSubController().receiveFriendResponse(user, response);
        }
    }

    public List<UUID> getConnectedUsers() {
        return connectedUsers;
    }

    public DATAtoIHMimpl(MainController app) {
        this.app = app;
    }
}
