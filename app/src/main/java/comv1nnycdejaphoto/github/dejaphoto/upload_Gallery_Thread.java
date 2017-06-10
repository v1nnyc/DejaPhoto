package comv1nnycdejaphoto.github.dejaphoto;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ken on 6/9/2017.
 */

public class upload_Gallery_Thread extends Thread{

    public upload_Gallery_Thread(){

    }

    public void run(){
        // Gets the current gallery and the user
        SharedPreferences sharedPreferences = BackgroundService.getContext().getSharedPreferences("DejaPhoto",MODE_PRIVATE);
        String json = sharedPreferences.getString("Gallery", "");
        String user = sharedPreferences.getString("User", "");
        if(json == ""){
            Log.v("Error","error");
            return;
        }
        // converts the json gallery to the actual gallery
        Gson gson = new Gson();
        Default_Gallery gallery = gson.fromJson(json, Default_Gallery.class);
        User current_user = gson.fromJson(user, User.class);
        FirebaseDatabase data = FirebaseDatabase.getInstance();

        // uploads the gallery to the current user's database
        gallery.upload_Gallery(current_user, BackgroundService.getContext(), data);

    }
}
