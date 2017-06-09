package comv1nnycdejaphoto.github.dejaphoto;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Vector;

/**
 * Created by Ken on 6/7/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class Friend_Load_Thread extends Thread{
    User use;
    Context con;
    FirebaseDatabase dat;
    Default_Gallery gal;
    SharedPreferences sharedPreference;

    Friend_Load_Thread(User user, Context context, Default_Gallery gall){
        use = user;
        con = context;
        gal = gall;
        sharedPreference = BackgroundService.getContext().getSharedPreferences("DejaPhoto",MODE_PRIVATE);
    }

    public void run(){
     //   dat = gal.upload_Gallery(use, con,dat);
     //   gal = new Default_Gallery();


        sharedPreference = BackgroundService.getContext().getSharedPreferences("DejaPhoto",MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreference.edit();

        Gson gson = new Gson();

        // downloads and get friend gallery
        Default_Gallery gallery = gal.download_Friends(use, con, dat);
        String json = gson.toJson(gallery);
        Log.v("Full Gallery inputted is", json);
        // stores friend gallery
        edit.putString("Gallery", json);
        Log.v("Finished Uploading Friends Gallery", "" + gallery.get_photos());

        // need to create an all gallery that merges Friend and Default Gallery

        edit.apply();
    }


}
