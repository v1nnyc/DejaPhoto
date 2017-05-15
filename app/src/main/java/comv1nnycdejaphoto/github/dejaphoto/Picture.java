package comv1nnycdejaphoto.github.dejaphoto;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by avery2 on 5/9/17.
 */

public class Picture {

    private String path;
    private String timeTaken;
    private  String dateTaken;
    private String location;
    private boolean karma;
    private boolean display;

    /**
     * Constructor for a Picture
     * pic - Imageview of the picture
     * time - time the picture was taken
     * loc - location where picture was taken
     * initialize karma to 0
     */
    public Picture(String pic, String date, String time, String loc) {
        path = pic;
        dateTaken = date;
        timeTaken = time;
        location = loc;
        karma = false;
        display = true;
    }

    /**
     * if user clicks on the karma button for a picture then karma is set to true
     * if picture already has karma, nothing changes
     */
    public void addKarma(){
        karma = true;
    }

    public void hide(){ display = false;}

<<<<<<< HEAD
    public String timetoString(){
        return "abc";
    }

    public boolean timeWithinBounds(double curr_time, int deviation){
        if( (timeTaken+deviation) <= curr_time )
            return true;

        if( (timeTaken-deviation) >= curr_time)
=======
    public boolean timeWithinBounds(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String TimeBefore = simpleDateFormat.format(calendar.get(Calendar.HOUR_OF_DAY)-2);
        String TimeAfter = simpleDateFormat.format(calendar.get(Calendar.HOUR_OF_DAY)-2);
        if(TimeBefore.compareTo(timeTaken) >= 0 && TimeAfter.compareTo(timeTaken) <=0 )
>>>>>>> 1a0bab856707dff6954de541b5d85ccb6b7450c7
            return true;

        return false;
    }

    //getters
    public String getImage(){
        return path;
    }
    public String getLocatio(){
        return location;
    }
    public boolean getKarma(){
        return karma;
    }
    public boolean getDisplay(){return display;}
    public String getTime(){return timeTaken;}
    public String getDate(){return dateTaken;}

    public boolean isEqual(Picture other){
        /*If other is null, it must be different then an exist object*/
        if(other == null)
            return false;
        File thisFile = new File(this.getImage());
        File otherFile = new File(other.getImage());
        Uri thisUri = Uri.fromFile(thisFile);
        Uri otherUri = Uri.fromFile(otherFile);
        try {
            Bitmap thisBitmap = MediaStore.Images.Media.getBitmap(BackgroundService.getContext().getContentResolver(), thisUri);
            Bitmap otherBitmap = MediaStore.Images.Media.getBitmap(BackgroundService.getContext().getContentResolver(), otherUri);
            if(thisBitmap.sameAs(otherBitmap))
                return true;
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*The try false, something went wrong so return false*/
        return false;
    };

    public boolean isEqual(Uri otherUri){
        /*If other is null, it must be different then an exist object*/
        if(otherUri == null)
            return false;
        File thisFile = new File(this.getImage());
        Uri thisUri = Uri.fromFile(thisFile);
        try {
            Bitmap thisBitmap = MediaStore.Images.Media.getBitmap(BackgroundService.getContext().getContentResolver(), thisUri);
            Bitmap otherBitmap = MediaStore.Images.Media.getBitmap(BackgroundService.getContext().getContentResolver(), otherUri);
            if(thisBitmap.sameAs(otherBitmap))
                return true;
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*The try false, something went wrong so return false*/
        return false;
    };
}
