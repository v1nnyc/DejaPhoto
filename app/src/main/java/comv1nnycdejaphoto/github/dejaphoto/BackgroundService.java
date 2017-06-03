package comv1nnycdejaphoto.github.dejaphoto;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Vector;

public class BackgroundService extends Service {
    private final IBinder iBinder = new LocalService();
    private static Context sContext;
    Default_Gallery defaultGallery;
    String mode;
    SharedPreferences sharedPreferences;

    /*Index of the image that is displaying.*/
    int index;

    /*How fast would the image change automatically*/
    int rate;

    public BackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }
  
    public static Context getContext() {
        return sContext;
    }
  
    class LocalService extends Binder{
        public BackgroundService getService(){
            return BackgroundService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sContext = getApplicationContext();

        final Handler handler = new Handler();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                /* Read the shared preferences*/
                readPreferences();

                /*Load the next picture by calling the gallery's method*/
                    //defaultGallery.Load_All(getContext());
                if(defaultGallery != null) {
                    defaultGallery.next();
                    handler.postDelayed(this, rate * 5000);
                }
            }
        };

        /*loop the task by delay it for rate*5000 millisecond*/
        handler.postDelayed(task, rate*5000);
        return START_STICKY;
    }

    public void readPreferences() {
        sharedPreferences = getContext().getSharedPreferences("DejaPhoto", MODE_PRIVATE);
        //sharedPreferences.edit().clear().apply();


        /*gson is a way to put the object into shared preferences*/

        Gson gson = new Gson();
        /*
        String json = sharedPreferences.getString("Gallery", "");
        defaultGallery = gson.fromJson(json, Default_Gallery.class);
        */
        defaultGallery = Default_Gallery.Choose_Gallery(getContext());
        if(defaultGallery == null) {
            defaultGallery = new Default_Gallery();
            defaultGallery.Load_All(getContext());
            Log.v("Number of photo beinng loaded", Integer.toString(defaultGallery.get_photos()));
            String json = gson.toJson(defaultGallery);
            sharedPreferences.edit().putString("Gallery", json).apply();
        }
        /*Save the value into shared preferences*/
        /*Index for last displayed image's index*/
        index = sharedPreferences.getInt("Index", 0);

        /*An User pick speed to change the image*/
        rate = sharedPreferences.getInt("Rate", 1);
        mode = sharedPreferences.getString("Mode","time");
        Log.v("mode",mode);
    }
}