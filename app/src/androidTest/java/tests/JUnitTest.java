package tests;


import android.app.Instrumentation;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import comv1nnycdejaphoto.github.dejaphoto.AddFrd;
import comv1nnycdejaphoto.github.dejaphoto.BackgroundService;
import comv1nnycdejaphoto.github.dejaphoto.GoogleSignInActivity;
import comv1nnycdejaphoto.github.dejaphoto.MainActivity;
import comv1nnycdejaphoto.github.dejaphoto.R;
import comv1nnycdejaphoto.github.dejaphoto.RateActivity;
import comv1nnycdejaphoto.github.dejaphoto.ViewShareOption;


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
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public ActivityTestRule<RateActivity> RateActivity = new ActivityTestRule<>(RateActivity.class);
    @Rule
    public ActivityTestRule<ViewShareOption> viewShareOption = new ActivityTestRule<>(ViewShareOption.class);
    @Rule
    public ActivityTestRule<AddFrd> addFrd = new ActivityTestRule<>(AddFrd.class);
    @Rule
    public ActivityTestRule<GoogleSignInActivity> signIn = new ActivityTestRule<>(GoogleSignInActivity.class);


    /* test on the start screen Choose Album button */
    @Test
    public void testChooseButton() throws Throwable {
        final Button button = (Button) mainActivity.getActivity().findViewById(R.id.choose);

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });

        assertEquals(button.callOnClick(), true);
    }

    /* test on the start screen Realse button */
    @Test
    public void testReleaseButton() throws Throwable {
        final Button button = (Button) mainActivity.getActivity().findViewById(R.id.release);

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });

        assertEquals(button.callOnClick(), true);
    }


    /* test on the start screen Setting button */
    @Test
    public void testSettingButton() throws Throwable {
        final ImageButton button = (ImageButton) mainActivity.getActivity().findViewById(R.id.setting);

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });

        assertEquals(button.callOnClick(), true);
    }

    /* test if clicking the setting button is going to the correct activity */
    @Test
    public void testSettingButtonActivity() throws Throwable {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor
                (RateActivity.class.getName(), null, false);

        // open current activity.
        final ImageButton button = (ImageButton) mainActivity.getActivity().findViewById(R.id.setting);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                button.performClick();
            }
        });

        assertEquals(button.callOnClick(), true);
    }

    /* test whether the TextView defined in the layout has Display Rate */
    @Test
    public void testDisplayRate() {
        TextView textView = (TextView) RateActivity.getActivity().findViewById(R.id.textView);
        String value = textView.getText().toString();
        assertEquals("Display Rate", value);
    }

    /* test whether the TextView defined in the layout has Display Mode */
    @Test
    public void testDisplayMode(){
        TextView textView = (TextView) RateActivity.getActivity().findViewById(R.id.mode);
        String value = textView.getText().toString();
        assertEquals("Display Mode", value);
    }

    /* test whether the TextView defined in the layout has 5 sec */
    @Test
    public void testDisplayDurationMin() {
        TextView textView = (TextView) RateActivity.getActivity().findViewById(R.id.min);
        String value = textView.getText().toString();
        assertEquals("5 sec", value);
    }

    /* test whether the TextView defined in the layout has 1 min */
    @Test
    public void testDisplayDurationMax() {
        TextView textView = (TextView) RateActivity.getActivity().findViewById(R.id.max);
        String value = textView.getText().toString();
        assertEquals("1 min", value);
    }

    /* test if the second radio button is checked */
    @Test
    public void testDayCheckbox() throws Throwable {
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
    public void testWeekCheckbox() throws Throwable {
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
    public void testTimeButton() {
        RadioButton radioButton = (RadioButton) RateActivity.getActivity().findViewById(R.id.time);
        String value = radioButton.getText().toString();
        assertEquals("Time", value);
    }

    /* check if the radio button option is correct for the second button */
    @Test
    public void testDayButton() throws Throwable{
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
    public void testWeekButton() throws Throwable {
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

    @Test
    public void testViewMyselfButton() {
        Button button = (Button) viewShareOption.getActivity().findViewById(R.id.viewMine);
        String value = button.getText().toString();
        assertEquals("ViewMySelf", value);
    }

    @Test
    public void testViewFrdButton() {
        Button button = (Button) viewShareOption.getActivity().findViewById(R.id.viewFrds);
        String value = button.getText().toString();
        assertEquals("ViewFrd", value);
    }

    @Test
    public void testShareButton() {
        Button button = (Button) viewShareOption.getActivity().findViewById(R.id.share);
        String value = button.getText().toString();
        assertEquals("Share", value);
    }

    @Test
    public void testPhotoPickerButton() throws Throwable {
        final Button button = (Button) mainActivity.getActivity().findViewById(R.id.picker);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });

        String value = button.getText().toString();
        assertEquals("Photos Picker", value);
    }

    @Test
    public void testAddFriendButton() throws Throwable {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor
                (AddFrd.class.getName(), null, false);

        // open current activity.
        final ImageButton button = (ImageButton) mainActivity.getActivity().findViewById(R.id.addFrd);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                button.performClick();
            }
        });

        //Watch for the timeout
        AddFrd add = (AddFrd) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);

        // next activity is opened and captured.
        assertNotNull(add);
        add.finish();
    }

    @Test
    public void testSignInButton() throws Throwable {
        // open current activity.
        final ImageButton button = (ImageButton) mainActivity.getActivity().findViewById(R.id.signin);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                button.performClick();
            }
        });

        assertNotNull(button.callOnClick());
    }
}