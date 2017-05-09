package comv1nnycdejaphoto.github.dejaphoto;

import java.io.IOException;
import android.app.Activity;
import android.app.WallpaperManager;


public class wallpaper extends Activity {

    private MainActivity ma = new MainActivity();

    public wallpaper(){}

    /*METHOD CHANGES SYSTEM WALLPAPER
        call by passing in a resource id
        changeWallpaper(R.drawable.hello);
        parameters: int resource, this NEEDS to be an ID for a DRAWABLE
        picture.
     */
    public void changeWallpaper(int resource){
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(ma.getContext());
        try {
            myWallpaperManager.setResource(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}