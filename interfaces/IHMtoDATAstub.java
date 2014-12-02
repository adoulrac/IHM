package IHM.interfaces;

import DATA.interfaces.IHMtoDATA;
import DATA.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * The Class IHMtoDATAstub.
 */
public class IHMtoDATAstub implements IHMtoDATA {
    
    /** The list users. */
    List<User> listUsers = new ArrayList<User>();
    List<User> listUsers2 = new ArrayList<User>();
    List<User> listUsers3 = new ArrayList<User>();

    /** The list groups. */
    List<Group> listGroups = new ArrayList<Group>();
    
    /** The pictures. */
    List<Picture> pictures = new ArrayList<>();
    
    /** The amis. */
    Group amis = new Group("Amis");
    Group famille = new Group("Famille");
    Group travail = new Group("Travail");

    
    /** The arthur. */
    User arthur = new User("avanceul", "password", "Arthur", "Van Ceulen", "arthur.jpg", "16/08/1991");
    
    /** The rachid. */
    User rachid = new User("adoulrac", "password", "Rachid", "Adoul", "rachid.jpg", "08/06/1992");
    
    User arthurt = new User("tranarth", "password", "Arthur", "Tran", "arthurt.jpg", "03/07/1991");

    
    /** The selim. */
    User selim = new User("selim", "selim", "Sélim", "Zénagui", "selim.jpg", "08/06/1992");

    /** The pic a. */
    Picture picA = new Picture("IHM/resources/logo.jpeg", null);
    
    /** The pic b. */
    Picture picB = new Picture("IHM/resources/avatar_icon.png", null);
    
    /** The pic c. */
    Picture picC = new Picture("IHM/resources/avatar_icon.png", null);
    
    /** The pic d. */
    Picture picD = new Picture("IHM/resources/logo.jpeg", null);

    /**
     * Instantiates a new IH mto dat astub.
     */
    public IHMtoDATAstub() {
        listUsers.add(arthur);
        listUsers.add(rachid);
        listUsers2.add(selim);
        listUsers2.add(arthurt);
        listUsers3.add(arthur);
        listUsers3.add(arthurt);
        amis.setUsers(listUsers);
        famille.setUsers(listUsers2);
        travail.setUsers(listUsers3);
        listGroups.add(amis);
        listGroups.add(famille);
        listGroups.add(travail);
        arthur.setListGroups(listGroups);
        rachid.setListGroups(listGroups);
        selim.setListGroups(listGroups);
        arthurt.setListGroups(listGroups);

        pictures.add(picA);
        pictures.add(picB);
        pictures.add(picC);
        pictures.add(picD);
        selim.setListPictures(pictures);
        arthur.setListPictures(pictures);
        rachid.setListPictures(pictures);
    }

    /**
     * Add a new comment for a picture .
     *
     * @param comment The comment
     */
    @Override
    public void addComment(Comment comment) { }

    /* (non-Javadoc)
     * @see DATA.interfaces.IHMtoDATA#addNote(DATA.model.Note)
     */
    @Override
    public void addNote(Note note) {

    }

    /**
     * Add a new group for the current user.
     *
     * @param group The new group
     */
    @Override
    public void addGroup(Group group)  {
        listGroups.add(group);
    }

    /**
     * Add a picture for the current user.
     *
     * @param picture The picture
     */
    @Override
    public void addPicture(Picture picture)  { }

    /**
     * Add a user in a group.
     *
     * @param user The user to add
     * @param group The group
     */
    @Override
    public void addUserInGroup(User user, Group group)  {
        group.getUsers().add(user);
    }

    /* (non-Javadoc)
     * @see DATA.interfaces.IHMtoDATA#acceptUserInGroup(DATA.model.User, DATA.model.Group)
     */
    @Override
    public void acceptUserInGroup(User user, Group group) {

    }

    /* (non-Javadoc)
     * @see DATA.interfaces.IHMtoDATA#refuseUser(DATA.model.User)
     */
    @Override
    public void refuseUser(User user) {

    }

    /**
     * Delete a group and all its users.
     *
     * @param group The gorup to delete
     */
    @Override
    public void deleteGroup(Group group)  {
        listGroups.remove(group);
    }

    /**
     * Delete a picture and all its comments, notes and tags.
     *
     * @param picture The picture to delete
     */
    @Override
    public void deletePicture(Picture picture)  { }

    /**
     * Delete the user from the group specified.
     *
     * @param user : The user to delete
     * @param group : The group
     */
    @Override
    public void deleteUserFromGroup(User user, Group group)  {
        group.getUsers().remove(user);
    }

    /**
     * Save the current user in a JSON file.
     */
    @Override
    public void export()  { }

    /**
     * Get the user information.
     *
     * @param idUser : The user ID
     * @param idRequest : The request ID (for IHM)
     * @return the user by id
     */
    @Override
    public void getUserById(UUID idUser, String idRequest)  { }

    /**
     * Get all the users in the specified group.
     *
     * @param g the g
     * @return The user list
     */
    @Override
    public List<User> getUsersInGroup(Group g)  {
        if (g.getNom().equals("Amis"))
            return listUsers;
        else
            return new ArrayList<User>();
    }

    /**
     * Gets the user not in group.
     *
     * @param group the group
     * @return : list of user
     * @Brief : Request the list of users who are not in this group
     */
    @Override
    public List<User> getUserNotInGroup(Group group)  {
        if (!group.getNom().equals("Amis"))
            return listUsers;
        else
            return new ArrayList<User>();
    }

    /**
     * Gets the group.
     *
     * @param group of the group searched
     * @return the Group searched
     * @Brief Request the Group with the name in param
     */
    @Override
    public Group getGroup(String group)  {
        if (group.equals("Amis")) {
            return amis;
        } else return null;
    }

    /**
     * Gets the groups.
     *
     * @return : list of all the group
     * @Brief : Request all the group of the current user
     */
    @Override
    public List<Group> getGroups()  {
        /*List<Group> lg = new ArrayList<Group>();
        lg.add(amis);*/
        return listGroups;
    }

    /**
     * Gets the groups user not in.
     *
     * @param user the user
     * @return : list of this group
     * @Brief : Request all the group where the user is not in
     */
    @Override
    public List<Group> getGroupsUserNotIn(User user)  {
        return new ArrayList<Group>();
    }

    /**
     * Gets the picture by id.
     *
     * @param picture : id of the picture
     * @param idRequest the id request
     * @return : None (catch exception)
     * @Brief : Request a picture (with the id) on the network
     */
    @Override
    public void getPictureById(UUID picture, int idRequest)  { }

    /**
     * Gets the pictures.
     *
     * @param user the user
     * @param idRequest the id request
     * @return the pictures
     * @Brief Request a user’s pictures on the network
     */
    @Override
    public void getPictures(User user, int idRequest)  { }

    @Override
    public void getPictures( List<Tag> listtag, int idRequest )
    {

    }

    /**
     * Gets the pictures.
     *
     * @param listtag the listtag
     * @param idRequest the id request
     * @return the pictures
     * @Brief Request a picture from a list of tags
     */
    //@Override
    //public void getPictures(List<String> listtag, int idRequest)  { }

    /**
     * Gets the pictures.
     *
     * @param idRequest the id request
     * @return the pictures
     * @Brief Request all the pictures from all connected users
     */
    @Override
    public void getPictures(int idRequest)  { }

    /**
     * Gets the current user.
     *
     * @return User
     * @Brief Visit current user’s profile
     */
    @Override
    public User getCurrentUser()  { return rachid; }

    /**
     * Import_.
     *
     * @param parameter the parameter
     * @return User
     * @Brief Import a user from JSON file’s path
     */
    @Override
    public User import_(String parameter)  { return rachid; }

    /**
     * Update picture.
     *
     * @param picture the picture
     * @Brief Update current picture
     */
    @Override
    public void updatePicture(Picture picture)  { }

    /**
     * Update profile.
     *
     * @param parameter the parameter
     * @Brief Update user in the JSON file
     */
    @Override
    public void updateProfile(User parameter)  { }

    /**
     * Sign Up a new user, start the server and connect the user.
     *
     * @param u : A light User with the login information
     * @return true : signup OK
     * 		   false : signup KO
     */
    @Override
    public boolean signup(User u) { return true; }

    /**
     * Connect the user, check the password and username.
     *
     * @param username the username
     * @param password the password
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

    /* (non-Javadoc)
     * @see DATA.interfaces.IHMtoDATA#getAllUsers()
     */
    @Override
    public List<Group> getAllUsers() {
        return null;
    }

    /**
     * Logout the current user
     * Save its state.
     *
     * @return true : OK
     * 			false : KO
     */
    @Override
    public boolean logout() { return true; }

    /**
     * Edit the current profile.
     *
     * @param u : The new light user with the new information
     * @return true : edit OK
     * 			false : edit KO
     */
    @Override
    public boolean editProfile(User u) { return true; }

}
