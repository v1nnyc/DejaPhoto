package comv1nnycdejaphoto.github.dejaphoto;

/**
 * Created by avery2 on 6/9/17.
 */

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * interface used for the picture class
 * used when changing from MS1 to MS2 when we added feature to the pictures
 * used for testing
 */
public interface IPicture {

    public void addKarma();
    public void addLikes();
    public void hide();

    public boolean timeWithinBounds();

    public void setLocation(String newLocation);

    public boolean isEqual(Picture other);

    public boolean isEqual(Uri otherUri);

    //getters
    //used for testing
    public String getImage();
    public String getLocatio();
    public boolean getKarma();
    public boolean getDisplay();
    public String getTime();
    public String getDate();
    public int getLikes();
}
