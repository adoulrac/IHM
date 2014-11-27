package IHM.interfaces;

import DATA.interfaces.IHMtoDATA;
import DATA.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IHMtoDATAstub implements IHMtoDATA {
    List<User> listUsers = new ArrayList<User>();
    List<Group> listGroups = new ArrayList<Group>();
    List<Picture> pictures = new ArrayList<>();
    Group amis = new Group("Amis");
    User arthur = new User("avanceul", "password", "Arthur", "Van Ceulen", "arthur.jpg", "16/08/1991");
    User rachid = new User("adoulrac", "password", "Rachid", "Adoul", "rachid.jpg", "08/06/1992");
    User selim = new User("selim", "selim", "Sélim", "Zénagui", "selim.jpg", "08/06/1992");

    Picture picA = new Picture("IHM/resources/logo.jpeg", null);
    Picture picB = new Picture("IHM/resources/avatar_icon.png", null);
    Picture picC = new Picture("IHM/resources/avatar_icon.png", null);
    Picture picD = new Picture("IHM/resources/logo.jpeg", null);

    public IHMtoDATAstub() {
        listUsers.add(arthur);
        listUsers.add(rachid);
        listUsers.add( selim );
        amis.setUsers(listUsers);
        listGroups.add(amis);
        arthur.setListGroups(listGroups);
        rachid.setListGroups(listGroups);
        selim.setListGroups( listGroups );

        pictures.add(picA);
        pictures.add(picB);
        pictures.add(picC);
        pictures.add(picD);
        selim.setListPictures(pictures);
        arthur.setListPictures(pictures);
        rachid.setListPictures(pictures);
    }

    /**
     * Add a new comment for a picture 
     * @param comment	The comment
     * @param idRequest	The idRequest
     */
    @Override
    public void addComment(Comment comment) { }

    @Override
    public void addNote(Note note) {

    }

    /**
     * Add a new group for the current user
     * @param group The new group
     */
    @Override
    public void addGroup(Group group)  { }

    /**
     * Add a picture for the current user
     * @param picture	The picture
     */
    @Override
    public void addPicture(Picture picture)  { }

    /**
     * Add a user in a group
     * @param user	The user to add
     * @param group	The group
     */
    @Override
    public void addUserInGroup(User user, Group group)  { }

    @Override
    public void acceptUserInGroup(User user, Group group) {

    }

    @Override
    public void refuseUser(User user) {

    }

    /**
     * Delete a group and all its users
     * @param group	The gorup to delete
     */
    @Override
    public void deleteGroup(Group group)  { }

    /**
     * Delete a picture and all its comments, notes and tags
     * @param picture	The picture to delete
     */
    @Override
    public void deletePicture(Picture picture)  { }

    /**
     * Delete the user from the group specified
     * @param user : The user to delete
     * @param group : The group
     */
    @Override
    public void deleteUserFromGroup(User user, Group group)  { }

    /**
     * Save the current user in a JSON file
     */
    @Override
    public void export()  { }

    /**
     * Get the user information
     * @param idUser	: The user ID
     * @param idRequest	: The request ID (for IHM)
     */
    @Override
    public void getUserById(UUID idUser, String idRequest)  { }

    /**
     * Get all the users in the specified group
     * @return	The user list
     */
    @Override
    public List<User> getUsersInGroup(Group g)  {
        if (g.getNom().equals("Amis"))
            return listUsers;
        else
            return new ArrayList<User>();
    }

    /**
     * @Brief : Request the list of users who are not in this group
     * @param : Group
     * @return : list of user
     */
    @Override
    public List<User> getUserNotInGroup(Group group)  {
        if (!group.getNom().equals("Amis"))
            return listUsers;
        else
            return new ArrayList<User>();
    }

    /**
     * @Brief Request the Group with the name in param
     * @param group of the group searched
     * @return the Group searched
     */
    @Override
    public Group getGroup(String group)  {
        if (group.equals("Amis")) {
            return amis;
        } else return null;
    }

    /**
     * @Brief : Request all the group of the current user
     * @param : None
     * @return : list of all the group
     */
    @Override
    public List<Group> getGroups()  {
        List<Group> lg = new ArrayList<Group>();
        lg.add(amis);
        return lg;
    }

    /**
     * @Brief : Request all the group where the user is not in
     * @param : user
     * @return : list of this group
     */
    @Override
    public List<Group> getGroupsUserNotIn(User user)  {
        return new ArrayList<Group>();
    }

    /**
     * @Brief : Request a picture (with the id) on the network 
     * @param picture : id of the picture
     * @return : None (catch exception)
     */
    @Override
    public void getPictureById(UUID picture, int idRequest)  { }

    /**
     * @Brief Request a user’s pictures on the network
     * @param user
     * @param idRequest
     */
    @Override
    public void getPictures(User user, int idRequest)  { }

    /**
     * @Brief Request a picture from a list of tags
     * @param listtag
     * @param idRequest
     */
    @Override
    public void getPictures(List<String> listtag, int idRequest)  { }

    /**
     * @Brief Request all the pictures from all connected users
     * @param idRequest
     */
    @Override
    public void getPictures(int idRequest)  { }

    /**
     * @Brief Visit current user’s profile
     * @return User
     */
    @Override
    public User getCurrentUser()  { return rachid; }

    /**
     * @Brief Import a user from JSON file’s path
     * @param parameter
     * @return User
     */
    @Override
    public User import_(String parameter)  { return rachid; }

    /**
     * @Brief Update current picture
     * @param picture
     */
    @Override
    public void updatePicture(Picture picture)  { }

    /**
     * @Brief Update user in the JSON file
     * @param parameter
     */
    @Override
    public void updateProfile(User parameter)  { }

    /**
     * Sign Up a new user, start the server and connect the user
     * @param u : A light User with the login information
     * @return true : signup OK
     * 		   false : signup KO
     */
    @Override
    public boolean signup(User u) { return true; }

    /**
     * Connect the user, check the password and username
     * @param username
     * @param password
     * @return true : Connect OK
     * 		 false : Connect KO
     */
    @Override
    public boolean login(String username, String password) {
        if ((username.equals("avanceul") || username.equals("adoulrac") || username.equals("selim"))
                && (password.equals("password") || password.equals( "selim" )))
            return true;
        else
            return false;
    }

    @Override
    public List<Group> getAllUsers() {
        return null;
    }

    /**
     * Logout the current user
     * Save its state
     * @return true : OK
     * 			false : KO
     */
    @Override
    public boolean logout() { return true; }

    /**
     * Edit the current profile
     * @param u : The new light user with the new information
     * @return	true : edit OK
     * 			false : edit KO
     */
    @Override
    public boolean editProfile(User u) { return true; }

}
