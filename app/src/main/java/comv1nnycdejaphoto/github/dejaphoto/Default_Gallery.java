package comv1nnycdejaphoto.github.dejaphoto;

/**
 * Created by Ken on 5/11/2017.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;
import java.util.List;
import java.util.Vector;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;


/**
 * Created by Ken on 5/9/2017.
 */


/* Got basic idea of how code will be like from
http://stackoverflow.com/questions/30777023/diplaying-all-images-from-device-inside-my-app
*/
public class Default_Gallery{

    private Vector<Picture> pictures = new Vector<Picture>();
    SharedPreferences load;
    private int num_photos = 0;


    public void Default_Gallery(){
    }

    public Vector<Picture> getPictures(){
        return pictures;
    }

    public void Load_All(Context context){

       /* Future implementation of load only on first call to app???
       load = context.getSharedPreferences("first", 0);
       SharedPreferences.Editor editor = load.edit();
       if( load.getBoolean("first", false) == false){
           editor.putBoolean("first", true);
           editor.commit();
       }
       else{
           return;
       }
       */

        Geocoder decoder = new Geocoder(context);            // one time creation for location finding
        Uri internal_storage = MediaStore.Images.Media.EXTERNAL_CONTENT_URI; // storage of all photos
        String [] data = {MediaStore.MediaColumns.DATA,                  // data to look for
                MediaStore.Images.ImageColumns.LATITUDE,
                MediaStore.Images.ImageColumns.LONGITUDE, MediaStore.Images.ImageColumns.DATE_TAKEN};
        Cursor finder = null;                                        // object that "points" to object

        if(internal_storage != null){
            finder = context.getContentResolver().query(internal_storage, data, null, null, null);
        }


        if(finder != null) {

            finder.moveToFirst();       // moves cursor to point to first image
            if(finder.getCount() == 0){
                return;
            }

            int i = 0;  // iteration/number of photos
            do{
                // finds specific location of indicated info (DATA or PATH in this case)
                // for the current photo, to retrieve as a string
                int data_index = finder.getColumnIndex(MediaStore.Images.Media.DATA);

                // gets the String data indicated at location by data_index
                String path = finder.getString(data_index);

                // default value for time if no time in photo (ie negative time)
                double time = MIN_VALUE;

                // default gps and location if no gps in photo
                double latitude = MAX_VALUE;
                double longitude = MAX_VALUE;
                String location = "No Location";

                // gets Date Taken data field and checks if it exists
                data_index = finder.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                if(data_index != -1 && finder.getString(data_index) != null) {

                    // if date taken exists, store date as a double
                    String date = finder.getString(data_index);
                    time = Double.parseDouble(date);
                }



                // gets the Latitude GPS data field and checks if it exists
                data_index = finder.getColumnIndex(MediaStore.Images.Media.LATITUDE);
                if(data_index != -1 && finder.getString(data_index) != null) {

                    // if latitude exists, store data of gps as double
                    latitude = Double.parseDouble(finder.getString(data_index));
                }



                // gets the Longitude GPS data field and checks if it exists
                data_index = finder.getColumnIndex(MediaStore.Images.Media.LONGITUDE);
                if(data_index != -1 && finder.getString(data_index) != null){
                    // if longitude exists, store gps as double
                    longitude = Double.parseDouble(finder.getString(data_index));
                }
                // finds path again
                data_index = finder.getColumnIndex(MediaStore.Images.Media.DATA);



                if(longitude != MAX_VALUE && latitude != MAX_VALUE){
                    // gets the location name depending on the gps
                    location = get_Location(context, latitude, longitude, decoder);
                }




                /**
                *** Part that possibily needs changing since Image view might be too big of an
                *** object to create and put in pictures, causing an out of bounds memory error
                ***
                 */
                ImageView img = new ImageView(context);
                img.setImageURI(Uri.parse(path));

                // creates a new "picture" and adds it to storage
                pictures.add(new Picture(img, (int) time, location));


                i++;
            }
            while(finder.moveToNext());
            num_photos = i;
        }
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
        Address add = address.get(0);

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

}