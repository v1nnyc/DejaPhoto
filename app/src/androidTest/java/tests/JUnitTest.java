package tests;



import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;

import comv1nnycdejaphoto.github.dejaphoto.MainActivity;
import comv1nnycdejaphoto.github.dejaphoto.R;
import comv1nnycdejaphoto.github.dejaphoto.RateActivity;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


/**
 * Created by ShirleyLam on 5/14/17.
 */

public class JUnitTest{

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
    @Rule
    public ActivityTestRule<RateActivity> RateActivity = new ActivityTestRule<RateActivity>(RateActivity.class);


    /* test on the start screen Choose Album button */
    @Test
    public void test2(){
        Button button = (Button) mainActivity.getActivity().findViewById(R.id.choose);
        button.callOnClick();
    }

    /* test on the start screen Realse button */
    @Test
    public void test3(){
        Button button = (Button) mainActivity.getActivity().findViewById(R.id.release);
        button.callOnClick();
    }

    /* test on the start screen Setting button */
    @Test
    public void test4(){
        Button button = (Button) mainActivity.getActivity().findViewById(R.id.setting);
        button.callOnClick();
    }

    /* test whether the TextView defined in the layout has Display Rate */
    @Test
    public void test5(){
        TextView textView = (TextView) RateActivity.getActivity().findViewById(R.id.textView);
        String value = textView.getText().toString();
        assertEquals("Display Rate", value);
    }

    /* test whether the TextView defined in the layout has Display Mode */
    @Test
    public void test6(){
        TextView textView = (TextView) RateActivity.getActivity().findViewById(R.id.mode);
        String value = textView.getText().toString();
        assertEquals("Display Mode", value);
    }

    /* test whether the TextView defined in the layout has 5 sec */
    @Test
    public void test7(){
        TextView textView = (TextView) RateActivity.getActivity().findViewById(R.id.min);
        String value = textView.getText().toString();
        assertEquals("5 sec", value);
    }

    /* test whether the TextView defined in the layout has 1 min */
    @Test
    public void test8(){
        TextView textView = (TextView) RateActivity.getActivity().findViewById(R.id.max);
        String value = textView.getText().toString();
        assertEquals("1 min", value);
    }

    /* test if the first radio button is checked */
    @Test
    public void test9() throws Exception {
        RadioButton radioButton = (RadioButton) RateActivity.getActivity().findViewById(R.id.time);
        assertTrue("The first radio button should be checked", radioButton.isChecked());

        RadioGroup group = (RadioGroup) RateActivity.getActivity().findViewById(R.id.radioGroup);
        assertEquals("The first radio button should be checked", R.id.time,
                group.getCheckedRadioButtonId());
    }

    /* check if the radio button option is correct for first button */
    @Test
    public void test10(){
        RadioButton radioButton = (RadioButton) RateActivity.getActivity().findViewById(R.id.time);
        String value = radioButton.getText().toString();
        assertEquals("Time", value);
    }






}
