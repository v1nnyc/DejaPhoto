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
    Default_Gallery defaultGallery;
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
    class LocalService extends Binder{
        public BackgroundService getService(){
            return BackgroundService.this;
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Handler handler = new Handler();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                /* Read the shared preferences*/
                readPreferences();
                /*Load the next picture by calling the gallery's method*/
                defaultGallery.next();
                if (true) {
                    handler.postDelayed(this , rate*5000);
                }
            }
        };
        /*loop the task by delay it for rate*5000 millisecond*/
        handler.postDelayed(task, rate*5000);
        return START_STICKY;
    }

    public void readPreferences() {
        sharedPreferences = MainActivity.getContext().getSharedPreferences("DejaPhoto",MODE_PRIVATE);
        /*gson is a way to put the object into shared preferences*/
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Gallery", "");
        defaultGallery = gson.fromJson(json, Default_Gallery.class);
        /*Index for last displayed image's index*/
        index = sharedPreferences.getInt("Index", 0);
        /*An User pick speed to change the image*/
        rate = sharedPreferences.getInt("Rate", 1);

    }
}