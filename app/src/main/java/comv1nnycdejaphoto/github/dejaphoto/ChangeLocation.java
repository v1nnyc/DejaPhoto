package comv1nnycdejaphoto.github.dejaphoto;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class ChangeLocation extends AppCompatActivity {

    Default_Gallery defaultGallery;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ImageButton button;
    private EditText editText;
    static int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changelocation);
        sharedPreferences = getSharedPreferences("DejaPhoto", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        readReference();
        initialize();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                readReference();
//                int index = sharedPreferences.getInt("ChangeIndexLocation",-2);
                if(index != -2){
                    String newLocation = editText.getText().toString();
                    defaultGallery.getPictures().elementAt(index).setLocation(newLocation);
                    Gson gson = new Gson();
                    String json = gson.toJson(defaultGallery);
                    /* Updated the Gallery*/
                    editor.putString("Gallery", json).apply();
                }
            }
        });



    }
    private void initialize(){
        editText = (EditText) findViewById(R.id.location);
        button = (ImageButton) findViewById(R.id.applychange);
        index = sharedPreferences.getInt("ChangeIndexLocation",-2);
        String location = "No Location";
        if(index != -2)
            location = defaultGallery.getPictures().elementAt(index).getLocatio();
        editText.setText(location);
    }
    private void readReference(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Gallery", "");
        defaultGallery = gson.fromJson(json, Default_Gallery.class);
    }
}
