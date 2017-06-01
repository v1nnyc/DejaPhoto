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
    //use the email of a user to find them on firebase and send a request
    public void add(String Id) {
    }

    @Override
    //use the email of a user to remove from our friendsList
    //do nothing if the user does not exist
    public void remove(String Id) {
        int i = 0;
        while(i < friendsList.size()){
            if(friendsList[i] == Id)
                friendsList.remove(i);
            i++;
        }
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
