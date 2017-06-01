package comv1nnycdejaphoto.github.dejaphoto;


import java.util.ArrayList;

/**
 * Created by VINCENT on 5/31/17.
 */

public class User implements Friend {
    fireBaseHandler handler = new fireBaseHandler();
    String Id = null;
    String name = null;
    ArrayList<User> friendsList;

    public User(){

        //IDK how to use googleSigninActivity
        //but this should be the email we sign in with
        Id = "test";
        name = "whatever";

    }

    @Override
    public void add(String Id) {
    }

    @Override
    public void remove(String Id) {

    }

    @Override
    public boolean handleRequest() {
        return false;
    }

    @Override
    public ArrayList<User> getFriendsList() {
        return this.friendsList;
    }
}
