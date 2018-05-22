package codezilla.foss.nirogya.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import javax.sql.DataSource;

import codezilla.foss.nirogya.R;
import codezilla.foss.nirogya.database.NirogyaDataSource;

public class ProfileActivity extends AppCompatActivity {

    private LinearLayout selectWeight, selectHeight;
    private TextView weightChoice, heightChoice;
    private Button nextButton;
    private Context currentContext;
    private LinearLayout parentLayout;
    private PopupWindow popupWindow;
    private static String height, weight;
    private String selectedGender;
    private NirogyaDataSource nirogyaDataSource;
    private String transferStringKey= "gender";
    private String userDetailsStatusKey = "ok";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        Bundle bundle = getIntent().getExtras();
        selectedGender = bundle.getString(transferStringKey);

        nirogyaDataSource = new NirogyaDataSource(this);
        nirogyaDataSource.open();

        selectHeight = findViewById(R.id.select_height);
        selectWeight = findViewById(R.id.select_weight);
        heightChoice = findViewById(R.id.height_choice);
        weightChoice = findViewById(R.id.weight_choice);
        nextButton = findViewById(R.id.next_button);

        // Get the application context
        currentContext = getApplicationContext();

        // Get the widgets reference from XML layout
        parentLayout = findViewById(R.id.activity_profile);

        selectHeight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) currentContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                // Inflate the activity_profile_height_selector layout/view
                View customView = inflater.inflate(R.layout.activity_profile_height_selector, null);
                /*
                    public PopupWindow (View contentView, int width, int height)
                        Create a new non focusable popup window which can display the contentView.
                        The dimension of the window must be passed to this constructor.

                        The popup does not provide any background. This should be handled by
                        the content view.

                    Parameters
                        contentView : the popup's content
                        width : the popup's width
                        height : the popup's height
                */
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                // Initialize a new instance of popup window
                popupWindow = new PopupWindow(
                        customView,
                        width - 200,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                // Set an elevation value for popup window
                // Call requires API level 21
                if (Build.VERSION.SDK_INT >= 21) {
                    popupWindow.setElevation(5.0f);
                }
                // Get a reference for the activity_profile_height_selector view close button
                Button cancelButton = customView.findViewById(R.id.fs_gender_cancel_button);
                // Get a reference for the activity_profile_height_selector view OK button
                Button okButton = customView.findViewById(R.id.fs_gender_ok_button);
                // Set a click listener for the popup window close button
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        popupWindow.dismiss();
                    }
                });
                // Set a click listener for the popup window OK button
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!(height == null)) {
                            heightChoice.setText(height);
                        }
                        // Dismiss the popup window
                        popupWindow.dismiss();
                    }
                });
                /*
                    public void showAtLocation (View parent, int gravity, int x, int y)
                        Display the content view in a popup window at the specified location. If the
                        popup window cannot fit on screen, it will be clipped.
                        Learn WindowManager.LayoutParams for more information on how gravity and the x
                        and y parameters are related. Specifying a gravity of NO_GRAVITY is similar
                        to specifying Gravity.LEFT | Gravity.TOP.

                    Parameters
                        parent : a parent view to get the getWindowToken() token from
                        gravity : the gravity which controls the placement of the popup window
                        x : the popup's x location offset
                        y : the popup's y location offset
                */
                // Finally, show the popup window at the center location of root relative layout
                popupWindow.showAtLocation(parentLayout, Gravity.CENTER, 0, 0);
                NumberPicker numberPlicker = customView.findViewById(R.id.numberPlicker);
                //Populate NumberPicker values from minimum and maximum value range
                //Set the minimum value of NumberPicker
                numberPlicker.setMinValue(100);
                //Specify the maximum value/number of NumberPicker
                numberPlicker.setMaxValue(250);
                //Gets whether the selector wheel wraps when reaching the min/max value.
                numberPlicker.setWrapSelectorWheel(true);
                //Set a value change listener for NumberPicker
                numberPlicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //Display the newly selected number from picker
                        Toast.makeText(ProfileActivity.this, String.valueOf(newVal), Toast.LENGTH_SHORT).show();
                        height = String.valueOf(newVal);
                    }
                });
            }
        });

        selectWeight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) currentContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the activity_profile_height_selector layout/view
                View customView = inflater.inflate(R.layout.activity_profile_weight_selector, null);

                /*
                    public PopupWindow (View contentView, int width, int height)
                        Create a new non focusable popup window which can display the contentView.
                        The dimension of the window must be passed to this constructor.

                        The popup does not provide any background. This should be handled by
                        the content view.

                    Parameters
                        contentView : the popup's content
                        width : the popup's width
                        height : the popup's height
                */
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                // Initialize a new instance of popup window
                popupWindow = new PopupWindow(
                        customView,
                        width - 200,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if (Build.VERSION.SDK_INT >= 21) {
                    popupWindow.setElevation(5.0f);
                }

                // Get a reference for the activity_profile_height_selector view close button
                Button cancelButton = customView.findViewById(R.id.fs_gender_cancel_button);

                // Get a reference for the activity_profile_height_selector view OK button
                Button okButton = customView.findViewById(R.id.fs_gender_ok_button);

                // Set a click listener for the popup window close button
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        popupWindow.dismiss();
                    }
                });
                // Set a click listener for the popup window OK button
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!(weight == null)) {
                            weightChoice.setText(weight);
                        }
                        // Dismiss the popup window
                        popupWindow.dismiss();
                    }
                });

                /*
                    public void showAtLocation (View parent, int gravity, int x, int y)
                        Display the content view in a popup window at the specified location. If the
                        popup window cannot fit on screen, it will be clipped.
                        Learn WindowManager.LayoutParams for more information on how gravity and the x
                        and y parameters are related. Specifying a gravity of NO_GRAVITY is similar
                        to specifying Gravity.LEFT | Gravity.TOP.

                    Parameters
                        parent : a parent view to get the getWindowToken() token from
                        gravity : the gravity which controls the placement of the popup window
                        x : the popup's x location offset
                        y : the popup's y location offset
                */
                // Finally, show the popup window at the center location of root relative layout
                popupWindow.showAtLocation(parentLayout, Gravity.CENTER, 0, 0);

                NumberPicker numberPlicker = customView.findViewById(R.id.numberPlicker);

                //Populate NumberPicker values from minimum and maximum value range
                //Set the minimum value of NumberPicker
                numberPlicker.setMinValue(30);
                //Specify the maximum value/number of NumberPicker
                numberPlicker.setMaxValue(150);

                //Gets whether the selector wheel wraps when reaching the min/max value.
                numberPlicker.setWrapSelectorWheel(true);

                //Set a value change listener for NumberPicker
                numberPlicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //Display the newly selected number from picker
                        Toast.makeText(ProfileActivity.this, String.valueOf(newVal), Toast.LENGTH_SHORT).show();
                        weight = String.valueOf(newVal);
                    }
                });
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (height != null && !height.isEmpty() && weight != null && !weight.isEmpty()) {
                    Log.d("check_data", selectedGender + " " + height + " " + weight);
                    nirogyaDataSource.insertDataUserDetails(selectedGender, height, weight, userDetailsStatusKey);
                    finish();
                    Intent intent = new Intent(ProfileActivity.this, TabContentActivity.class);
                    startActivity(intent);
                } else {
                    final AlertDialog alertDialog = new AlertDialog.Builder(
                            ProfileActivity.this,R.style.Theme_Dialog).create();
                    // Setting Dialog Title
                    alertDialog.setTitle(getResources().getString(R.string.profile_activity_title_text));
                    // Setting Dialog Message
                    alertDialog.setMessage(getResources().getString(R.string.profile_activity_message_text));
                    // Setting OK Button
                    alertDialog.setButton(getResources().getString(R.string.profile_activity_ok_button_text), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                }
            }
        });
    }
}
