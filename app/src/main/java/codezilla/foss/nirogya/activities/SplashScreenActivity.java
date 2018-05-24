package codezilla.foss.nirogya.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import codezilla.foss.nirogya.Constant;
import codezilla.foss.nirogya.R;
import codezilla.foss.nirogya.database.NirogyaDataSource;

public class SplashScreenActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private NirogyaDataSource nirogyaDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        nirogyaDataSource = new NirogyaDataSource(this);
        nirogyaDataSource.open();
        final String status = nirogyaDataSource.getStatusFromUserDetails("1");
        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer.
             */
            @Override
            public void run() {
                // This will be executed once the timer is over
                if (status.equals(Constant.userDetailsTableConfiguration)) {
                    finish();
                    Intent intent = new Intent(SplashScreenActivity.this, TabContentActivity.class);
                    startActivity(intent);
                } else {
                    // Start your app main activity
                    Intent intent = new Intent(SplashScreenActivity.this, SelectGenderActivity.class);
                    startActivity(intent);
                    // close this activity
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
