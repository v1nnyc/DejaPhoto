package comv1nnycdejaphoto.github.dejaphoto;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ServiceConnection;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static Context sContext;

    //constructor
    public MainActivity(){}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sContext = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

        //changing wallpaper
        wallpaper wp = new wallpaper();
        wp.changeWallpaper(R.drawable.hi);
    }

    /*required for other classes to be able to access MainActivity
     */
    public static Context getContext() {
        return sContext;
    }
}
