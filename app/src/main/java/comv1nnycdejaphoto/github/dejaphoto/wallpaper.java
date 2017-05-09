package comv1nnycdejaphoto.github.dejaphoto;

import java.io.IOException;
import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

public class wallpaper extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //call method with a drawable id
        changeWallpaper(R.drawable.hello);
    }

    /*METHOD CHANGES SYSTEM WALLPAPER
        call by passing in a resource id
        changeWallpaper(R.drawable.hello);
        parameters: int resource, this NEEDS to be an ID for a DRAWABLE
        picture.
     */
    public void changeWallpaper(int resource){
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());
        try {
            myWallpaperManager.setResource(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}