package co.com.stohio.openshift.main;

//import android.Manifest;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import co.com.stohio.openshift.R;

public class MyIntro extends AppIntro2 {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add your slide's fragments here
        // AppIntro will automatically generate the dots indicator and buttons.

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
        addSlide(AppIntroFragment.newInstance("inter pet", "Collecting your real time data and outputting a cute puppy! ", R.drawable.neutral, Color.parseColor("#2E7D32")));
        addSlide(AppIntroFragment.newInstance("Sick", "If you consistently make bad choices, your pet starts getting sick! ", R.drawable.sick, Color.parseColor("#2979FF")));
        addSlide(AppIntroFragment.newInstance("Happy", "However, if you consistently do great things he will be happy! ", R.drawable.happy, Color.parseColor("#FDD835")));
        addSlide(AppIntroFragment.newInstance("Tired", "When getting low sleep sleep Fido will reflect that! ", R.drawable.refactor, Color.parseColor("#42A5F5")));
        addSlide(AppIntroFragment.newInstance("Bloated", "And if you eat fast food or don't get groceries, he starts feeling bloated :( ", R.drawable.bloated, Color.parseColor("#0D47A1")));

        // OPTIONAL METHODS

        // Override bar/separator color
//        setBarColor(Color.parseColor("#3F51B5"));
//        setSeparatorColor(Color.parseColor("#2196F3"));

        // SHOW or HIDE the statusbar
        showStatusBar(false);

        // Edit the color of the nav bar on Lollipop+ devices
//        setNavBarColor(Color.BLUE);

        // Hide Skip/Done button
//        showSkipButton(false);
//        showDoneButton(false);

        // Turn vibration on and set intensity
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest
//        setVibrate(true);
//        setVibrateIntensity(30);

        // Animations -- use only one of the below. Using both could cause errors.
//        setFadeAnimation(); // OR
//        setZoomAnimation(); // OR
//        setFlowAnimation(); // OR
//        setSlideOverAnimation(); // OR
//        setDepthAnimation(); // OR
//        setCustomTransformer(yourCustomTransformer);

        // Permissions -- takes a permission and slide number
//        askForPermissions(new String[]{Manifest.permission.CAMERA}, 3);
    }

    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
        finish();

    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something when slide is changed
    }
}
