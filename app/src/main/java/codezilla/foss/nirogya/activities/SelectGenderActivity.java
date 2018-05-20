package codezilla.foss.nirogya.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import codezilla.foss.nirogya.R;
import codezilla.foss.nirogya.database.NirogyaDataSource;

public class SelectGenderActivity extends AppCompatActivity {

    private Button genderMale,genderFemale,nextButton;
    private TextView femaleText, maleText;
    private ImageView femaleChoice,maleChoice;
    private static  String selectedGender;
    private NirogyaDataSource nirogyaDataSource;
    private String maleSignification = "male";
    private String femaleSignification = "female";
    private String defaultDateFormat = "yyyy/MM/dd HH:mm:ss";
    private String indexDefaultValue ="1";
    private String defaultStepGoal = "6000";
    private String defaultSensitivity = "10";
    private String transferStringKey= "gender";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_gender);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }

        nirogyaDataSource = new NirogyaDataSource(this);
        nirogyaDataSource.open();

        genderFemale = findViewById(R.id.gender_female);
        genderMale = findViewById(R.id.gender_male);
        nextButton = findViewById(R.id.next_button);
        femaleText =findViewById(R.id.female_text);
        maleText = findViewById(R.id.male_text);
        femaleChoice = findViewById(R.id.female_choice);
        maleChoice = findViewById(R.id.male_choice);

        genderFemale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                genderFemale.setBackgroundResource(R.drawable.female_button_clicked_shape);
                genderMale.setBackgroundResource(R.drawable.default_gender_buttons_shape);
                nextButton.setVisibility(View.VISIBLE);
                femaleText.setTextColor(getResources().getColor(R.color.select_gender_activity_selected_button_text_color));
                maleText.setTextColor(getResources().getColor(R.color.select_gender_activity_unselected_button_text_color));
                femaleChoice.setVisibility(View.VISIBLE);
                maleChoice.setVisibility(View.GONE);
                selectedGender = femaleSignification;
            }
        });

        genderMale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                genderMale.setBackgroundResource(R.drawable.male_button_clicked_shape);
                genderFemale.setBackgroundResource(R.drawable.default_gender_buttons_shape);
                nextButton.setVisibility(View.VISIBLE);
                maleText.setTextColor(getResources().getColor(R.color.select_gender_activity_selected_button_text_color));
                femaleText.setTextColor(getResources().getColor(R.color.select_gender_activity_unselected_button_text_color));
                maleChoice.setVisibility(View.VISIBLE);
                femaleChoice.setVisibility(View.GONE);
                selectedGender = maleSignification;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DateFormat dateFormat = new SimpleDateFormat(defaultDateFormat);
                Date date = new Date();
                String subscribeDate =  dateFormat.format(date);
                nirogyaDataSource.deleteTableTwitterTips();
                nirogyaDataSource.deleteTablePedometer();
                nirogyaDataSource.insertDataTwitterData(indexDefaultValue,subscribeDate,null,null);
                nirogyaDataSource.insertDataPedometer(indexDefaultValue,null,defaultStepGoal,null,null,null,null,defaultSensitivity);
                finish();
                Intent intent = new Intent(SelectGenderActivity.this,ProfileActivity.class);
                intent.putExtra(transferStringKey,selectedGender);
                startActivity(intent);
            }
        });


    }
}
