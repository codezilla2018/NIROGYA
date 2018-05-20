package codezilla.foss.nirogya.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import codezilla.foss.nirogya.R;

public class AchievementActivity extends AppCompatActivity {

    private Button targetAcheivement;
    private Button bronzeMedal, silverMedal, goldMedal;
    private LinearLayout silverMedalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        targetAcheivement = findViewById(R.id.targetAchievement);
        bronzeMedal = findViewById(R.id.bronzeMedal);
        silverMedal = findViewById(R.id.silverMedal);
        goldMedal = findViewById(R.id.goldMedal);
        silverMedalLayout = findViewById(R.id.silverMedalLayout);
        bronzeMedal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                targetAcheivement.setBackgroundResource(R.drawable.bronze);
            }
        });
        silverMedal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                targetAcheivement.setBackgroundResource(R.drawable.silver);
            }
        });
        goldMedal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                targetAcheivement.setBackgroundResource(R.drawable.gold);
            }
        });
    }
}
