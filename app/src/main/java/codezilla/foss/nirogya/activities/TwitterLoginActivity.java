package codezilla.foss.nirogya.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import codezilla.foss.nirogya.R;
import codezilla.foss.nirogya.helpers.SharedPreferenceManager;

public class TwitterLoginActivity extends AppCompatActivity {

    private String consumerKey = "nW88XLuFSI9DEfHOX2tpleHbR";
    private String consumerSecret = "hCg3QClZ1iLR13D3IeMvebESKmakIelp4vwFUICuj6HAfNNCer";
    private String screenName = "nirogya";
    private SharedPreferenceManager sharedPreferenceManager;
    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initiate Twitter config
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(consumerKey, consumerSecret))//pass Twitter API Key and Secret
                .debug(true)
                .build();
        Twitter.initialize(config);
        sharedPreferenceManager = new SharedPreferenceManager(this);
        //check if user is already login or not
        if (sharedPreferenceManager.getUserId() != 0) {
            //if already login then start main activity
            startMainActivity();
            return;
        }
        setContentView(R.layout.activity_twitter_login);
        loginButton = findViewById(R.id.login_button);
        //set the callback to Twitter login button
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                TwitterSession twitterSession = result.data;
                //if twitter session is not null then save user data to shared preference
                if (twitterSession != null) {
                    sharedPreferenceManager.saveUserId(twitterSession.getUserId());//save user id
                    sharedPreferenceManager.saveScreenName(screenName);//save user screen name
                    //after saving start main activity
                    startMainActivity();
                    //show toast
                    Toast.makeText(TwitterLoginActivity.this, getResources().getString(R.string.twitter_login_activity_successful_login_message), Toast.LENGTH_SHORT).show();
                } else {
                    //if twitter session is null due to some reason then show error toast
                    Toast.makeText(TwitterLoginActivity.this, getResources().getString(R.string.twitter_login_activity_unsuccessful_login_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(TwitterLoginActivity.this, getResources().getString(R.string.twitter_login_activity_unsuccessful_login_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * method to start Main Activity
     */
    private void startMainActivity() {
        startActivity(new Intent(this, TimelineActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
