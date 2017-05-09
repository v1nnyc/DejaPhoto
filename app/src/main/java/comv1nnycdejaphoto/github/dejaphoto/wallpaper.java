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
    Bitmap bitmap;
    int lastImageRef;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        changeWallpaper(R.drawable.hello);
    }

    public void changeWallpaper(int icon){
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());
        try {
            myWallpaperManager.setResource(icon);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}