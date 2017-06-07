package comv1nnycdejaphoto.github.dejaphoto;


import java.util.ArrayList;

/**
 * Created by VINCENT on 5/31/17.
 */

public class User {
    fireBaseHandler handler = new fireBaseHandler();
    String myID = null;
    String name = null;
    ArrayList<String> friendsList;

    public User(){

        //IDK how to use googleSigninActivity
        //but this should be the email we sign in with
        myID = "test";
        name = "whatever";

    }

    /** use the id of a user to find them on firebase and send a request
     * does not add user to list of friends until they accept
     * @param id - id of user to send request to
     * @return whether the id was found on firebase
     */
    public boolean sendRequest(String id) {
        //find user with the id on firebase
        if(!handler.search(id))
            return false;
        //send request to that id

        return true;
    }

    /** receives friend request and accepts
     * adds friends to each users friend list
     * @param id
     */
    public void acceptRequest(String id){

    }

    //use the email of a user to remove from our friendsList
    //do nothing if the user does not exist
    public void remove(String Id) {
        int i = 0;
        while(i < friendsList.size()){
            if(friendsList.get(i)== myID)
                friendsList.remove(i);
            i++;
        }
    }

    public boolean handleRequest() {
        return false;
    }

    public ArrayList<String> getFriendsList() {
        return this.friendsList;
    }
}
