package comv1nnycdejaphoto.github.dejaphoto;


import android.support.v7.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Created by VINCENT on 5/31/17.
 */

public class User extends AppCompatActivity{
    DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://dejaphoto-ee08f.firebaseio.com/");
    fireBaseHandler handler = new fireBaseHandler();
    private String myID = null;
    private String name = null;
    private ArrayList<String> friendsList;

    public User(){

        //IDK how to use googleSigninActivity
        //but this should be the email we sign in with


    }

    /** getters and setters **/
    public void setMyID(String n){
        myID = n;
    }
    public void setMyName(String n){
        name = n;
    }
    public String getMyID(){
        return myID;
    }
    public String getName(){
        return name;
    }
    public ArrayList<String> getFriendsList(){
        return friendsList;
    }

    /** use the id of a user to find them on firebase and send a request
     * does not add user to list of friends until they accept
     * @param email - id of user to send request to
     * @return whether the id was found on firebase
     */
    public boolean sendRequest(String email) {
        //find user with the id on firebase
        Query query = database.equalTo(email);

            return false;
        //send request to that id

        //return true;
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

}
