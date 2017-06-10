package comv1nnycdejaphoto.github.dejaphoto;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Ken on 6/8/2017.
 */

// Another Picture Class that actually stores the encode Bitmap String as image
public class Database_Picture {

    private String image;
    private String timeTaken;
    private  String dateTaken;
    private String location;
    private boolean karma;
    private boolean display;
    private int likes;

    /**
     * Constructor for a Picture
     * pic - Imageview of the picture
     * time - time the picture was taken
     * loc - location where picture was taken
     * initialize karma to 0
     */
    public Database_Picture(String img, Picture pic) {
        image = img;
        dateTaken = pic.getDate();
        timeTaken = pic.getTime();
        location = pic.getLocatio();
        karma = false;
        display = true;
        likes = pic.getLikes();
    }

    /**
     * if user clicks on the karma button for a picture then karma is set to true
     * if picture already has karma, nothing changes
     */
    public void addKarma(){
        karma = true;
    }
    /* Increase the number of "Karma"*/
    public void addLikes() { likes++; }
    public void hide(){ display = false;}

    //getters
    public String getImage(){
        return image;
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
    public int getLikes() { return likes;}

    public void setLocation(String newLocation){
        location = newLocation;
    }

}
