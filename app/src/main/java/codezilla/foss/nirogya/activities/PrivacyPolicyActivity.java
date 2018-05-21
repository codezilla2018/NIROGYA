package codezilla.foss.nirogya.activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import codezilla.foss.nirogya.R;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private TextView privacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        privacyPolicy = findViewById(R.id.privacy_policy);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            privacyPolicy.setText(Html.fromHtml(getResources().getString(R.string.privacy_policy_activity_html_file), Html.FROM_HTML_MODE_LEGACY));
        }else {
            privacyPolicy.setText(Html.fromHtml(getResources().getString(R.string.privacy_policy_activity_html_file)));
        }
    }
}
