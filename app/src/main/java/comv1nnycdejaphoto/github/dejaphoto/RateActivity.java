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

public class RateActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("DejaPhoto", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setContentView(R.layout.rate);

        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        final TextView textView = (TextView) findViewById(R.id.rateView);
        int progress = sharedPreferences.getInt("Rate", 0);
        seekBar.setProgress(progress);
        seekBar.setMax(11);

        textView.setText("Display Rate: " + (seekBar.getProgress() + 1) * 5);
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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked) {
                if (checked == R.id.time) {
                    editor.putString("Mode","time");
                    Toast.makeText(getApplicationContext(), "Set mode: Time", Toast.LENGTH_SHORT).show();
                } else if (checked == R.id.day) {
                    editor.putString("Mode","day");
                    Toast.makeText(getApplicationContext(), "Set mode: Day", Toast.LENGTH_SHORT).show();
                } else {
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



}