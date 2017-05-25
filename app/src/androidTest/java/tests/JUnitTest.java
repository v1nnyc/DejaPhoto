package tests;


import android.app.Instrumentation;
import android.support.test.annotation.UiThreadTest;
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


import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;


/**
 * Created by ShirleyLam on 5/14/17.
 */

public class JUnitTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
    @Rule
    public ActivityTestRule<RateActivity> RateActivity = new ActivityTestRule<RateActivity>(RateActivity.class);

    /* test on the start screen Choose Album button */
    @Test
    public void test1() {
        Button button = (Button) mainActivity.getActivity().findViewById(R.id.choose);
        button.callOnClick();
    }

    /* test on the start screen Realse button */
    @Test
    public void test2() {
        Button button = (Button) mainActivity.getActivity().findViewById(R.id.release);
        button.callOnClick();
    }
    

    /* test on the start screen Setting button */
    @Test
    public void test3() {
        Button button = (Button) mainActivity.getActivity().findViewById(R.id.setting);
        button.callOnClick();
    }

    /* test if clicking the setting button is going to the correct activity */
    @Test
    public void test4() throws Throwable {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor
                (RateActivity.class.getName(), null, false);

        // open current activity.
        final Button button = (Button) mainActivity.getActivity().findViewById(R.id.setting);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                button.performClick();
            }
        });

        //Watch for the timeout
        RateActivity rateActivity = (RateActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);

        // next activity is opened and captured.
        assertNotNull(rateActivity);
        rateActivity.finish();
    }

    /* test whether the TextView defined in the layout has Display Rate */
    @Test
    public void test5() {
        TextView textView = (TextView) RateActivity.getActivity().findViewById(R.id.textView);
        String value = textView.getText().toString();
        assertEquals("Display Rate", value);
    }

    /* test whether the TextView defined in the layout has Display Mode */
    @Test
    public void test6() {
        TextView textView = (TextView) RateActivity.getActivity().findViewById(R.id.mode);
        String value = textView.getText().toString();
        assertEquals("Display Mode", value);
    }

    /* test whether the TextView defined in the layout has 5 sec */
    @Test
    public void test7() {
        TextView textView = (TextView) RateActivity.getActivity().findViewById(R.id.min);
        String value = textView.getText().toString();
        assertEquals("5 sec", value);
    }

    /* test whether the TextView defined in the layout has 1 min */
    @Test
    public void test8() {
        TextView textView = (TextView) RateActivity.getActivity().findViewById(R.id.max);
        String value = textView.getText().toString();
        assertEquals("1 min", value);
    }

    /* test if the first radio button is checked */
    @Test
    public void test9() throws Exception {
        RadioButton radioButton = (RadioButton) RateActivity.getActivity().findViewById(R.id.time);
        assertTrue("The first radio button should be checked", radioButton.isChecked());

        RadioGroup radioGroup = (RadioGroup) RateActivity.getActivity().findViewById(R.id.radioGroup);
        assertEquals("The first radio button should be checked", R.id.time,
                radioGroup.getCheckedRadioButtonId());
    }

    /* test if the second radio button is checked */
    @Test
    public void test10() throws Throwable {
        final RadioButton radioButton = (RadioButton) RateActivity.getActivity().findViewById(R.id.day);
        RateActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click the second radio button
                radioButton.performClick();
            }
        });
        assertTrue(radioButton.isChecked());
    }

    /* test if the third radio button is checked */
    @Test
    public void test11() throws Throwable {
        final RadioButton radioButton = (RadioButton) RateActivity.getActivity().findViewById(R.id.week);
        RateActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click the second radio button
                radioButton.performClick();
            }
        });
        assertTrue(radioButton.isChecked());
    }

    /* check if the radio button option is correct for the first button */
    @Test
    public void test12() {
        RadioButton radioButton = (RadioButton) RateActivity.getActivity().findViewById(R.id.time);
        String value = radioButton.getText().toString();
        assertEquals("Time", value);
    }

    /* check if the radio button option is correct for the second button */
    @Test
    public void test13() throws Throwable{
        final RadioButton radioButton = (RadioButton) RateActivity.getActivity().findViewById(R.id.day);
        RateActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click the second radio button
                radioButton.performClick();
            }
        });
        String value = radioButton.getText().toString();
        assertEquals("Day", value);
    }

    /* check if the radio button option is correct for the third button */
    @Test
    public void test14() throws Throwable {
        final RadioButton radioButton = (RadioButton) RateActivity.getActivity().findViewById(R.id.week);
        RateActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click the second radio button
                radioButton.performClick();
            }
        });
        String value = radioButton.getText().toString();
        assertEquals("Week", value);
    }
}