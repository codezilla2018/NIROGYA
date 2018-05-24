package codezilla.foss.nirogya.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnticipateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import codezilla.foss.nirogya.R;
import codezilla.foss.nirogya.database.NirogyaDataSource;

public class AchievementActivity extends AppCompatActivity {

    private Button targetAcheivement;
    private Button bronzeMedal, silverMedal, goldMedal;
    private TextView targetAchievementPoints;
    private int decoViewBackIndex;
    private int decoViewSeriesIndex1;
    private DecoView decoView;
    private float docoviewMax = 10000f;
    private int decoViewSeriesIndex2;
    private int decoViewSeriesIndex3;
    private NirogyaDataSource nirogyaDataSource;
    private float currentStepCount;
    private String defaultTableRowIndexValue = "1";
    private int decoviewEvent1Duration = 3000;
    private int decoviewEvent1Delay = 100;
    private int decoviewEvent2Duration = 2000;
    private int decoviewEvent2Delay = 1250;
    private int decoviewEvent3Delay = 3250;
    private int decoviewEvent4Duration = 1000;
    private int decoviewEvent4Delay = 7000;
    private int decoviewEvent5Delay = 8500;
    private float decoviewEvent5EndPosition = 16.3f;
    private int decoviewEvent6Duration = 1000;
    private int decoviewEvent6Delay = 12500;
    private int decoviewEvent7Duration = 1000;
    private int decoviewEvent7Delay = 20000;
    private int decoviewEvent8Duration = 3000;
    private int decoviewEvent8Delay = 21000;
    private String decoviewEvent8DisplayText = "GOAL!";
    private float decoviewEvent9EndPosition = 4.36f;
    private int decoviewEvent9Delay = 14000;
    private int decoviewEvent10Delay = 18000;
    private int decoviewEvent11Delay = 18000;
    private String textToGoPrefix = "%.1f steps to goal \n (bronze medal)";
    private String percentageFormat = "%.0f%%";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        nirogyaDataSource = new NirogyaDataSource(AchievementActivity.this);
        nirogyaDataSource.open();
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        String[] retrievedData = nirogyaDataSource.getAllDataFromPedometer(defaultTableRowIndexValue);
        if (retrievedData[1] == null || retrievedData[1].isEmpty()) {
            currentStepCount = 0;
        } else {
            int savedSteps = Integer.parseInt(retrievedData[1]);
            currentStepCount = savedSteps;
        }
        decoView = findViewById(R.id.dynamicArcView);
        // Create required data series on the DecoView
        createBackSeries();
        createDataSeries1();
        createEvents();
        targetAcheivement = findViewById(R.id.targetAchievement);
        targetAchievementPoints = findViewById(R.id.targetAchievementPoints);
        bronzeMedal = findViewById(R.id.bronzeMedal);
        silverMedal = findViewById(R.id.silverMedal);
        goldMedal = findViewById(R.id.goldMedal);
        bronzeMedal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                targetAcheivement.setBackgroundResource(R.drawable.activity_achievement_bronze_medal);
                targetAcheivement.setText(getResources().getString(R.string.achievement_activity_target_1_text));
                targetAchievementPoints.setText(getResources().getString(R.string.achievement_activity_target_point_1));
            }
        });
        silverMedal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                targetAcheivement.setBackgroundResource(R.drawable.activity_achievement_silver_medal);
                targetAcheivement.setText(getResources().getString(R.string.achievement_activity_target_2_text));
                targetAchievementPoints.setText(getResources().getString(R.string.achievement_activity_target_point_2));
            }
        });
        goldMedal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                targetAcheivement.setBackgroundResource(R.drawable.activity_achievement_gold_medal);
                targetAcheivement.setText(getResources().getString(R.string.achievement_activity_target_3_text));
                targetAchievementPoints.setText(getResources().getString(R.string.achievement_activity_target_point_3));
            }
        });
    }

    private void createBackSeries() {
        SeriesItem seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.achievement_activity_doco_view_color_1))
                .setRange(0, docoviewMax, 0)
                .setInitialVisibility(true)
                .build();
        decoViewBackIndex = decoView.addSeries(seriesItem);
    }

    private void createDataSeries1() {
        final SeriesItem seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.achievement_activity_doco_view_color_2))
                .setRange(0, docoviewMax, 0)
                .setInitialVisibility(false)
                .build();
        final TextView textPercentage = findViewById(R.id.text_percentage);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                textPercentage.setText(String.format(percentageFormat, percentFilled * 100f));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {
            }
        });
        final TextView textToGo = findViewById(R.id.text_remaining);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textToGo.setText(String.format(textToGoPrefix, seriesItem.getMaxValue() - currentPosition));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {
            }
        });
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {
            }
        });
        decoViewSeriesIndex1 = decoView.addSeries(seriesItem);
    }

    private void createEvents() {
        decoView.executeReset();
        decoView.addEvent(new DecoEvent.Builder(docoviewMax)
                .setIndex(decoViewBackIndex)
                .setDuration(decoviewEvent1Duration)
                .setDelay(decoviewEvent1Delay)
                .build());
        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(decoViewSeriesIndex1)
                .setDuration(decoviewEvent2Duration)
                .setDelay(decoviewEvent2Delay)
                .build());
        decoView.addEvent(new DecoEvent.Builder(currentStepCount)
                .setIndex(decoViewSeriesIndex1)
                .setDelay(decoviewEvent3Delay)
                .build());
        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(decoViewSeriesIndex2)
                .setDuration(decoviewEvent4Duration)
                .setEffectRotations(1)
                .setDelay(decoviewEvent4Delay)
                .build());
        decoView.addEvent(new DecoEvent.Builder(decoviewEvent5EndPosition)
                .setIndex(decoViewSeriesIndex2)
                .setDelay(decoviewEvent5Delay)
                .build());
        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(decoViewSeriesIndex3)
                .setDuration(decoviewEvent6Duration)
                .setEffectRotations(1)
                .setDelay(decoviewEvent6Delay)
                .build());
        decoView.addEvent(new DecoEvent.Builder(decoviewEvent9EndPosition)
                .setIndex(decoViewSeriesIndex3).setDelay(decoviewEvent9Delay).build());
        decoView.addEvent(new DecoEvent.Builder(0)
                .setIndex(decoViewSeriesIndex3).setDelay(decoviewEvent10Delay).build());
        decoView.addEvent(new DecoEvent.Builder(0)
                .setIndex(decoViewSeriesIndex2).setDelay(decoviewEvent11Delay).build());
        decoView.addEvent(new DecoEvent.Builder(0)
                .setIndex(decoViewSeriesIndex1)
                .setDelay(decoviewEvent7Delay)
                .setDuration(decoviewEvent7Duration)
                .setInterpolator(new AnticipateInterpolator())
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {
                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {
                        resetText();
                    }
                })
                .build());
        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                .setIndex(decoViewSeriesIndex1)
                .setDelay(decoviewEvent8Delay)
                .setDuration(decoviewEvent8Duration)
                .setDisplayText(decoviewEvent8DisplayText)
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {
                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {
                        createEvents();
                    }
                })
                .build());
        resetText();
    }

    private void resetText() {
        ((TextView) findViewById(R.id.text_percentage)).setText("");
        ((TextView) findViewById(R.id.text_remaining)).setText("");
    }
}
