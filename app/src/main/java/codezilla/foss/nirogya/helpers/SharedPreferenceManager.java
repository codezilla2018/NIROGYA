package codezilla.foss.nirogya.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {

    /**
     * preference constant keys
     */
    private static final String PREF_KEY = "login_prefs";
    private static final String USER_ID = "user_id";
    private static final String SCREEN_NAME = "screen_name";

    //preference manager instance
    private SharedPreferences sharedPreferences;

    public SharedPreferenceManager(Context context) {
        //initialize preference
        sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    }

    /**
     * method to save user id
     *
     * @param userId to save
     */
    public void saveUserId(Long userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(USER_ID, userId);
        editor.apply();
    }

    /**
     * @return saved user id if exist else return 0
     */
    public Long getUserId() {
        return sharedPreferences.getLong(USER_ID, 0);
    }

    /**
     * method to save user screen name
     *
     * @param screenName of the user
     */
    public void saveScreenName(String screenName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SCREEN_NAME, screenName);
        editor.apply();
    }

    /**
     * @return saved user screen name
     */
    public String getScreenName() {
        return sharedPreferences.getString(SCREEN_NAME, "");
    }
}
