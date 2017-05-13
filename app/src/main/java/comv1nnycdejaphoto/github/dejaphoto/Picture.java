package comv1nnycdejaphoto.github.dejaphoto;

import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by avery2 on 5/9/17.
 */

public class Picture {

    private String path;
    private double timeTaken;
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
    public Picture(String pic, int time, String loc) {
        path = pic;
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

    public String timetoString(){
        return "abc";
    }

    public boolean timeWithinBounds(double curr_time, int deviation){
        if( (timeTaken+deviation) <= curr_time )
            return true;
        if( (timeTaken-deviation) >= curr_time)
            return true;
        return false;
    }

    //getters
    public String getImage(){
        return path;
    }
    public double gettimeTaken(){
        return timeTaken;
    }
    public String getLocatio(){
        return location;
    }
    public boolean getKarma(){
        return karma;
    }
    public boolean getDisplay(){return display;}

}
