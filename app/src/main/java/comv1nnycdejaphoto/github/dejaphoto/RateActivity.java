package comv1nnycdejaphoto.github.dejaphoto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class RateActivity extends AppCompatActivity {
    Default_Gallery defaultGallery;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private SeekBar seekBar;
    private RadioGroup radioGroup;
    private TextView textView;
    int progress;
    String mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("DejaPhoto", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setContentView(R.layout.rate);
        readLastReference();
        initialize();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* display the value according to the seekbar */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("Display Rate : " + (progress + 1) * 5);
                editor.putInt("Rate", progress + 1);
                editor.apply();
            }

            /* display toast msg when user starts changing display rate */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(RateActivity.this, "Changing Display Rate", Toast.LENGTH_LONG).show();
            }

            /* display toast msg when user finished changing display rate */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(RateActivity.this, "Set Display Rate", Toast.LENGTH_LONG).show();
            }

        });

        /* Everytime clicked on on button, sort the pictures*/
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked) {
                if (checked == R.id.time) {
                    if(mode != "time"){
                        Gson gson = new Gson();
                        defaultGallery.sortByTime();
                        String json = gson.toJson(defaultGallery);
                        sharedPreferences.edit().putString("Gallery", json);
                    }
                    editor.putString("Mode","time");
                    Toast.makeText(getApplicationContext(), "Set mode: Time", Toast.LENGTH_SHORT).show();
                } else if (checked == R.id.day) {
                    if(mode != "day"){
                        Gson gson = new Gson();
                        defaultGallery.sortByTime();
                        String json = gson.toJson(defaultGallery);
                        sharedPreferences.edit().putString("Gallery", json);
                    }
                    editor.putString("Mode","day");
                    Toast.makeText(getApplicationContext(), "Set mode: Day", Toast.LENGTH_SHORT).show();
                } else {
                    if(mode != "week"){
                        Gson gson = new Gson();
                        defaultGallery.sortByTime();
                        String json = gson.toJson(defaultGallery);
                        sharedPreferences.edit().putString("Gallery", json);
                    }
                    editor.putString("Mode","week");
                    Toast.makeText(getApplicationContext(), "Set mode: Week", Toast.LENGTH_SHORT).show();
                }
                editor.apply();
            }
        });
    }



    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, Setting.class));
        finish();
    }
    /* Read the value from shared preference*/
    private void readLastReference(){
        progress = sharedPreferences.getInt("Rate", 0);
        mode = sharedPreferences.getString("Mode","time");

    }
    /*Initialize the page*/
    private void initialize(){
        textView = (TextView) findViewById(R.id.rateView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        seekBar.setProgress(progress);
        seekBar.setMax(11);
        textView.setText("Display Rate: " + (seekBar.getProgress() + 1) * 5);
        if(mode.equals("time"))
            radioGroup.check(R.id.time);
        if(mode.equals("day"))
            radioGroup.check(R.id.day);
        if(mode.equals("week"))
            radioGroup.check(R.id.week);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Gallery", "");
        defaultGallery = gson.fromJson(json, Default_Gallery.class);
    }
}