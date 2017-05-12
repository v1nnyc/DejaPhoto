package comv1nnycdejaphoto.github.dejaphoto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class RateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate);
        Log.v("sdf","sdf");

        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        final TextView textView = (TextView) findViewById(R.id.rateView);
        seekBar.setProgress(0);
        seekBar.setMax(12);

        textView.setText("Display Rate: " + seekBar.getProgress());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int progress_value;

            /* display the value according to the seekbar */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("Display Rate : " + progress*5);
            }

            /* display toast msg when user starts changing display rate */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(RateActivity.this,"Changing Display Rate",Toast.LENGTH_LONG).show();
            }

            /* display toast msg when user finished changing display rate */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(RateActivity.this,"Set Display Rate",Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }


}
