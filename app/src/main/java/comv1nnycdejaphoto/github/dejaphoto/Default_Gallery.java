package comv1nnycdejaphoto.github.dejaphoto;

/**
 * Created by Ken on 5/11/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Ref;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import comv1nnycdejaphoto.github.dejaphoto.Picture;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

/**
 * Created by Ken on 5/9/2017.
 */

/* Got basic idea of how code will be like from
http://stackoverflow.com/questions/30777023/diplaying-all-images-from-device-inside-my-app
*/
public class Default_Gallery{
    Default_Gallery defaultGallery;
    boolean no_show = false;
    SharedPreferences sharedPreferences;

    int index;
    private Vector<Picture> pictures = new Vector<Picture>();
    private int num_photos;

    public Vector<Picture> getPictures(){
        return pictures;
    }

    public void Load_All(Context context){
        Log.v("Loading","ALL");

        Geocoder decoder = new Geocoder(context);            // one time creation for location finding
        Uri internal_storage = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()); // storage of all photos
        Log.v("Path of internal_storage",internal_storage.getPath());
        File directory = new File(internal_storage.getPath());
        /* Filter to read only images*/
        File[] files = directory.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
            }
        });
        if(files != null){
            /* Loop through the  images*/
            for(File f : files){
                String fileName = f.getName(); // this is file name
                try {
                    /* Read the file with exifinterface*/
                    ExifInterface exifInterface = new ExifInterface(f.getAbsolutePath());
                    String time = exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
                    String date = exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
                    String latitude =  null;
                    String longitude = null;
                    String latitudeRef =  null;
                    String longitudeRef = null;
                    String location = "No Location";
                    /* The format will be xx/x,xx/x,xxxx/xxxx*/
                    latitude= exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                    longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                    /* It will be either 'W' 'E or 'N' 'S' */
                    latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                    longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
                    /*If they are all not null, it means they have location infromation*/
                    if(latitude != null && longitude != null && latitudeRef != null && longitudeRef != null) {
                        double latiString = conversion(latitude,latitudeRef);
                        double longString = conversion(longitude,longitudeRef);
                        Log.i("Lati", Double.toString(latiString));
                        Log.i("Long",Double.toString(longString));
                        location = get_Location(context,latiString,longString,decoder);
                    }
                    Log.i("FIle name", fileName);
                    Log.i("Location" , location);
                    pictures.add(new Picture(f.getPath(), date, time, location));
                    num_photos++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public double conversion(String converThis,String ref){
        /* Format : xx/yy,aa/bb,cccc/dddd or something like this*/
        String temp = converThis;
        double ans = 0;
        /* temp now will be xx*/
        temp = converThis.substring(0, converThis.indexOf('/'));
        ans = Double.parseDouble(temp);
        /* converThis will be ,aa/bb,cccc/dddd*/
        converThis = converThis.substring(converThis.indexOf(','));
        /* temp = aa*/
        temp = converThis.substring(1, converThis.indexOf('/'));
        ans = ans +  Double.parseDouble(temp)/60;
        /*converThis = aa/bb,cccc/dddd*/
        converThis = converThis.substring(1);
        /*converThis = ,cccc/dddd*/
        converThis = converThis.substring(converThis.indexOf(','));
        /* temp = cccc*/
        temp = converThis.substring(1,converThis.indexOf('/'));
        /* converThis = /dddd*/
        converThis = converThis.substring(converThis.indexOf('/'));
        /* converThis = dddd */
        converThis = converThis.substring(1);
        ans = ans + Double.parseDouble(temp)/3600 / Double.parseDouble(converThis);
        if(ref.compareTo("S") == 0 || ref.compareTo("W") == 0)
            ans = ans* -1;
        return ans;
    }

    public File retrieve_Photo(String Path) {
        File retrieved_image = new File(Path);  // finds image through file
        if(retrieved_image.exists()){
            return retrieved_image;
        }
        else{
            return null;
        }

    }

    public int get_photos(){
        return num_photos;
    }

    public String get_Location(Context context, double latitude, double longitude, Geocoder decoder){
        decoder = new Geocoder(context);  // using the geocoder
        List<Address> address = null;
        try { // try and get the address location from gps
            address = decoder.getFromLocation(latitude, longitude, 1);
        }
        catch(Exception e) {
            // ignore (bad code style) but used to fix case of adjusting all methods to throw exception
        }

        // using the address data type retrieved from gps
        // check various location types and outputs the most suitable name first
        if(address == null)
            return "No Location";
        Address add = address.get(0);

        //obtain address details
        return getAddressDetails(add);
    }

    //this method get detail information of the address
    public String getAddressDetails(Address add) {
        // names are made sure that they aren't entirely just numbers
        if(add.getFeatureName() != null && !add.getFeatureName().matches("[0-9]+")) {
            return add.getFeatureName();
        }
        else if(add.getAddressLine(0)!= null && !add.getAddressLine(0).matches("[0-9]+")) {
            return add.getAddressLine(0);
        }
        else if(add.getSubLocality() != null && !add.getSubLocality().matches("[0-9]+")) {
            return add.getSubLocality();
        }
        else if(add.getLocality() != null && !add.getLocality().matches("[0-9]+")) {
            return add.getLocality();
        }
        else if(add.getSubAdminArea() != null && !add.getSubAdminArea().matches("[0-9]+")) {
            return add.getSubAdminArea();
        }
        else if(add.getAdminArea() != null && !add.getAdminArea().matches("[0-9]+")){
            return add.getAdminArea();
        }
        else {
            return "No Discernible Location Name";
        }
    }

    public void next(){
        readPreferences();
        wallpaper wp = new wallpaper();
        if (defaultGallery.get_photos() != 0) {
            int last = index;
            SharedPreferences.Editor editor = sharedPreferences.edit();

            while ((index + 1) != last) {
                if (index == (defaultGallery.get_photos() - 1))
                    index = -1;
                Log.d("# photos", "" + defaultGallery.get_photos());
                Log.d("index" , "" + index);
                if ( defaultGallery.getPictures().elementAt(index + 1).getDisplay()) {
                    Picture picture = defaultGallery.getPictures().elementAt(index+1);
                    File file = new File(picture.getImage());
                    Uri uriFromGallery = Uri.fromFile(file);
                    wp.changeWallpaper(uriFromGallery, picture.getLocatio(),picture.getLikes());
                    editor.putInt("Index", index + 1);
                    break;
                }
                index = index + 1;
            }
            Log.v("Displaying",Integer.toString(index));
            editor.apply();
        }
        else wp.emptyPicture();
    }

    public void readPreferences(){
        /* Read the shared preferences*/
        sharedPreferences = BackgroundService.getContext().getSharedPreferences("DejaPhoto",MODE_PRIVATE);
        /*gson is a way to put the object into shared preferences*/

        /*
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Gallery","");
        defaultGallery = gson.fromJson(json, Default_Gallery.class);
        */
        defaultGallery = Default_Gallery.Choose_Gallery(BackgroundService.getContext());

        index = sharedPreferences.getInt("Index",0);
    }

    /* Used to choose one of 3 galleries we store in shared preferences */
    public static Default_Gallery Choose_Gallery(Context context){
        boolean friend = false;
        boolean user = true;
        Gson gson = new Gson();
        SharedPreferences sharedPreference = context.getSharedPreferences("DejaPhoto",MODE_PRIVATE);
        SharedPreferences sharedPref_View = context.getSharedPreferences("ViewShareOption",MODE_PRIVATE);
        String json;
        Default_Gallery gall = null;

        //Log.v("Check Box Values", sharedPref_View.getString("Frd", "null") + " " + sharedPref_View.getString("View","null"));
        friend = sharedPref_View.getString("Frd","").equals("frd");
        user = sharedPref_View.getString("View","").equals("self");
        //  SharedPreferences.Editor editor = sharedPreferences.edit();

        if(friend && user){
            //json = sharedPreference.getString("All", "");       // change names of galleries if need to
            //gall = gson.fromJson(json, Default_Gallery.class);
            Log.v("Uploaded Both friends and user galleries", "ALL");
        }
        else if(user){
            //json = sharedPreference.getString("Gallery","");
            //gall = gson.fromJson(json, Default_Gallery.class);
            Log.v("Uploaded just user gallery", "User");
        }
        else if(friend) {
            //json = sharedPreference.getString("Friends", "");
            //gall = gson.fromJson(json, Default_Gallery.class);
            Log.v("Uploaded just friend gallery", "Friend");
        }
        else{
            Log.v("Uploaded no gallery", "None");
        }
        json = sharedPreference.getString("Gallery","");
        gall = gson.fromJson(json, Default_Gallery.class);
        return gall;

    }

    public void sortByTime(){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("hh:mm:ss");
        readPreferences();
        int token = 0;
        /* If the time is within 2 hours, move them to the front*/
        for(int i = 0; i<defaultGallery.get_photos();++i){
            if(defaultGallery.getPictures().elementAt(i).timeWithinBounds()) {
                if (token == defaultGallery.get_photos() - 2) {
                    break;
                }
                else{
                    Collections.swap(defaultGallery.pictures,i,token);
                    token++;
                }
            }
        }
        /*For the photo with the same condiction, move the one with karma to the front*/
        int karmaCount = 0;
        for(int i = 0; i < token; ++i){
            if(defaultGallery.getPictures().elementAt(i).getKarma()){
                Collections.swap(defaultGallery.pictures,i,karmaCount);
                karmaCount++;
            }
        }
        /*Compare two picture's day, move the one took earlier to the back, for non karma pictures*/
        for(int i = 0; i < karmaCount-1; ++i){
            for(int j = 1; j <karmaCount; ++j) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        /* do the same thing for karma pictures*/
        for(int i = karmaCount; i < (token -1); i++){
            for(int j = karmaCount + 1; j < token ; j++) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        /*Do the same for match mode condition*/
        for(int i = token ; i < defaultGallery.get_photos()-1; ++i) {
            for (int j = token + 1; j < defaultGallery.get_photos(); j++) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*Same as sort By time*/
    public void sortByDay(){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("MM:dd");
        Date today = Calendar.getInstance().getTime();
        today.setYear(0);
        readPreferences();
        int token  = 0;
        for(int i = 0; i<defaultGallery.get_photos(); ++i){
            try {
                Date dateToken = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getDate());
                dateToken.setYear(0);
                if(today.equals(dateToken)){
                    Collections.swap(defaultGallery.pictures, i, token);
                    token++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int karmaCount = 0;
        for(int i = 0; i < token; ++i){
            if(defaultGallery.getPictures().elementAt(i).getKarma()){
                Collections.swap(defaultGallery.pictures,i,karmaCount);
                karmaCount++;
            }
        }
        for(int i = 0; i < karmaCount-1; ++i){
            for(int j = 1; j <karmaCount; ++j) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        for(int i = karmaCount; i < (token -1); i++){
            for(int j = karmaCount + 1; j < token ; j++) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        for(int i = token ; i < defaultGallery.get_photos()-1; ++i) {
            for (int j = token + 1; j < defaultGallery.get_photos(); j++) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void sortByWeek(){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy:MM:dd");
        Calendar today = Calendar.getInstance();
        today.setTime(today.getTime());
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        int token = 0;
        for(int i = 0; i < defaultGallery.get_photos() ; ++i){
            try {
                Calendar weekToken = Calendar.getInstance();
                Date took = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getDate());
                weekToken.setTime(took);
                if(dayOfWeek == weekToken.get(Calendar.DAY_OF_WEEK)) {
                    Collections.swap(defaultGallery.pictures, i, token);
                    token++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }int karmaCount = 0;
        for(int i = 0; i < token; ++i){
            if(defaultGallery.getPictures().elementAt(i).getKarma()){
                Collections.swap(defaultGallery.pictures,i,karmaCount);
                karmaCount++;
            }
        }
        for(int i = 0; i < karmaCount-1; ++i){
            for(int j = 1; j <karmaCount; ++j) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        for(int i = karmaCount; i < (token -1); i++){
            for(int j = karmaCount + 1; j < token ; j++) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        for(int i = token ; i < defaultGallery.get_photos()-1; ++i) {
            for (int j = token + 1; j < defaultGallery.get_photos(); j++) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}