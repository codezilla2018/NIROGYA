package codezilla.foss.nirogya.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fangxu.allangleexpandablebutton.AllAngleExpandableButton;
import com.fangxu.allangleexpandablebutton.ButtonData;
import com.fangxu.allangleexpandablebutton.ButtonEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import codezilla.foss.nirogya.Constant;
import codezilla.foss.nirogya.R;
import codezilla.foss.nirogya.StepDetector;
import codezilla.foss.nirogya.activities.AchievementActivity;
import codezilla.foss.nirogya.activities.InstructionsActivity;
import codezilla.foss.nirogya.database.NirogyaDataSource;
import codezilla.foss.nirogya.interfaces.StepListener;

import static android.content.Context.SENSOR_SERVICE;
import static codezilla.foss.nirogya.BurntCalories.findBurntCalories;

/**
 * Created by chinthaka on 5/17/18.
 */

public class StepsFragment extends Fragment implements SensorEventListener, StepListener {
    private TextView countSteps;
    private TextView calories_burnt;
    private TextView miles;
    private TextView stepGoal;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static int numSteps;
    private Button btnStart, btnStop;
    private static NumberFormat formatter = new DecimalFormat("#0.0000");
    private View fragment_steps;
    private NirogyaDataSource nirogyaDataSource;
    private static double height;
    private static double weight;
    private String[] retrivedData;
    private String defaultTableRowIndexValue = "1";
    private String pedometerTableStatusFieldExecutionValue = "start";
    private String pedometerTableStatusFieldPauseValue = "stop";
    private String defaultStepGoal = "6000";
    private String defaultSensitivity = "10";
    private String initialCountValue = "0";

    public static StepsFragment newInstance() {
        StepsFragment fragment = new StepsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nirogyaDataSource = new NirogyaDataSource(getActivity());
        nirogyaDataSource.open();
        String[] getUserData = nirogyaDataSource.getDataUserDetails(defaultTableRowIndexValue);
        height = Double.parseDouble(getUserData[1]);
        weight = Double.parseDouble(getUserData[2]);
        retrivedData = nirogyaDataSource.getAllDataFromPedometer(defaultTableRowIndexValue);
        Constant.STEP_THRESHOLD = Float.parseFloat(retrivedData[5]);
        if (retrivedData[1] == null || retrivedData[1].isEmpty()) {
            numSteps = 0;
        } else {
            int savedSteps = Integer.parseInt(retrivedData[1]);
            numSteps = savedSteps;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragment_steps = inflater.inflate(R.layout.fragment_steps, container, false);
        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        countSteps = fragment_steps.findViewById(R.id.human_steps);
        stepGoal = fragment_steps.findViewById(R.id.step_goal);
        btnStart = fragment_steps.findViewById(R.id.btn_start);
        btnStop = fragment_steps.findViewById(R.id.btn_stop);
        calories_burnt = fragment_steps.findViewById(R.id.calories_burnt);
        miles = fragment_steps.findViewById(R.id.miles);
        stepGoal = fragment_steps.findViewById(R.id.step_goal);
        stepGoal.setText(retrivedData[4]);

        if (retrivedData[0] != null) {
            if (retrivedData[0].equals(pedometerTableStatusFieldExecutionValue)) {
                countSteps.setText(retrivedData[1]);
                calories_burnt.setText(formatter.format(Double.parseDouble(retrivedData[3])));
                miles.setText(formatter.format(Double.parseDouble(retrivedData[2])));
                sensorManager.registerListener(StepsFragment.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                btnStart.setVisibility(View.GONE);
                btnStop.setVisibility(View.VISIBLE);
            } else {
                countSteps.setText(retrivedData[1]);
                calories_burnt.setText(formatter.format(Double.parseDouble(retrivedData[3])));
                miles.setText(formatter.format(Double.parseDouble(retrivedData[2])));
            }
        }


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                numSteps = 0;
                sensorManager.registerListener(StepsFragment.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                btnStart.setVisibility(View.GONE);
                btnStop.setVisibility(View.VISIBLE);
            }
        });


        btnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                nirogyaDataSource.updateStatusOfPedomter(defaultTableRowIndexValue, pedometerTableStatusFieldPauseValue);
                sensorManager.unregisterListener(StepsFragment.this);
                btnStop.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);

            }
        });
        installButton90to90();

        // Inflate the layout for this fragment
        return fragment_steps;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        countSteps.setText(String.valueOf(numSteps));
        double[] returnVal = findBurntCalories(numSteps, height, weight);
        nirogyaDataSource.updatePedomter(defaultTableRowIndexValue, pedometerTableStatusFieldExecutionValue, String.valueOf(numSteps), Double.toString(returnVal[1]), Double.toString(returnVal[0]));
        calories_burnt.setText(formatter.format(returnVal[0]));
        miles.setText(formatter.format(returnVal[1]));

    }

    public void installButton90to90() {
        final AllAngleExpandableButton button = fragment_steps.findViewById(R.id.button_expandable_90_90);
        final List<ButtonData> buttonDatas = new ArrayList<>();
        int[] drawable = {R.drawable.plus, R.drawable.mark, R.drawable.settings, R.drawable.heart};
        int[] color = {R.color.blue, R.color.red, R.color.green, R.color.yellow};
        for (int i = 0; i < 4; i++) {
            ButtonData buttonData;
            if (i == 0) {
                buttonData = ButtonData.buildIconButton(getActivity(), drawable[i], 15);
            } else {
                buttonData = ButtonData.buildIconButton(getActivity(), drawable[i], 0);
            }
            buttonData.setBackgroundColorId(getActivity(), color[i]);
            buttonDatas.add(buttonData);
        }
        button.setButtonDatas(buttonDatas);
        setListener(button);
    }

    private void setListener(AllAngleExpandableButton button) {
        button.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
                switch (index) {
                    case 1: {
                        Intent intent = new Intent(getActivity(), AchievementActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 2:
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setMessage(getResources().getString(R.string.steps_fragment_reset_data_dialog_message));
                        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.steps_fragment_reset_data_dialog_ok_button),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        nirogyaDataSource.resetPedomter(defaultTableRowIndexValue, null, defaultStepGoal, null, null, null, null, defaultSensitivity);
                                        btnStop.setVisibility(View.GONE);
                                        btnStart.setVisibility(View.VISIBLE);
                                        sensorManager.unregisterListener(StepsFragment.this);
                                        numSteps = 0;
                                        countSteps.setText(initialCountValue);
                                        calories_burnt.setText(initialCountValue);
                                        miles.setText(initialCountValue);

                                    }
                                });

                        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.steps_fragment_reset_data_dialog_cancel_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        break;
                    case 3:
                        Intent intent = new Intent(getActivity(), InstructionsActivity.class);
                        startActivity(intent);
                        break;
                    default: {
                    }

                }
            }

            @Override
            public void onExpand() {
                // showToast("onExpand");
            }

            @Override
            public void onCollapse() {
                // showToast("onCollapse");
            }
        });
    }

    private void showToast(String toast) {
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }
}