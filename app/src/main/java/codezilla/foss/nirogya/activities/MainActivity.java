package codezilla.foss.nirogya.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import codezilla.foss.nirogya.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    public void pedometer(View view){
        finish();
        Intent intent = new Intent(this,PedometerActivity.class);
        startActivity(intent);
    }
}
