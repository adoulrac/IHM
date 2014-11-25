package IHM;

import DATA.model.Group;
import DATA.model.User;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * This class has to be removed and is used only for tests
 */
public class Tester {

    public static List<Group> getStaticGroups() {

        List<Group> groups = Arrays.asList(new Group("Amis"), new Group("Famille"));
        groups.get(0).setUsers(getStaticUsers());

        return groups;
    }

    public static List<User> getStaticUsers() {
        List<User> users = Arrays.asList(new User("adoulrac", "Adoul12$", "Rachid", "Adoul", "", "13/06/1992"),
                                         new User("zenaguisel", "Zenagui12$", "Sélim", "Zénagui", "", "13/12/1992"));
        return users;
    }
}
