package comv1nnycdejaphoto.github.dejaphoto;


import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by VINCENT on 5/31/17.
 */

public class User {
    private String myID = null;
    private String name = null;
    private ArrayList<String> friendsList;
    private HashSet pending;
    private Default_Gallery myGallery;
    private String galleryJson = null;
    public User(){
        //IDK how to use googleSigninActivity
        //but this should be the email we sign in with
    }


    /** getters and setters **/
    public void addFreind(String n){pending.add("n");}
    public void setMyID(String n){
        myID = n;
    }
    public void setMyName(String n){name = n;}
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
     * @param id - id of user to send request to
     * @return whether the id was found on firebase
     */
    public void sendRequest(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myFirebaseRef = database.getReference();
        myFirebaseRef.child(id).child("Pending").setValue(getName());
        Log.v("User class send Request", "SENDIONG");
    }
    public boolean isFriend(String n){
        for(int i = 0; i < friendsList.size(); ++i){
            if(pending.contains(friendsList.get(i)))
                return true;
        }
        return false;
    }

    public void uploadDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myFirebaseRef = database.getReference();
        myFirebaseRef.child(getName()).setValue(this);
        Gson gson= new Gson();
        String json = gson.toJson(this);
        SharedPreferences sharedPreferences = BackgroundService.getContext().getSharedPreferences("DejaPhoto", MODE_PRIVATE);
        sharedPreferences.edit().putString("User", json).apply();
        Log.v("Uploaded to DataBase", "Updated sharepref");
    }

    public void setMyGallery(Default_Gallery gallery){
        Gson gson = new Gson();
        galleryJson = gson.toJson(gallery);
        myGallery = gallery;
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
