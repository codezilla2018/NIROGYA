package codezilla.foss.nirogya.fragments;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import codezilla.foss.nirogya.R;
import codezilla.foss.nirogya.activities.InstructionsActivity;
import codezilla.foss.nirogya.activities.PrivacyPolicyActivity;
import codezilla.foss.nirogya.database.NirogyaDataSource;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by chinthaka on 5/17/18.
 */

public class SettingsFragment extends Fragment {
    private View fragmentSettings;
    private LinearLayout stepGoalSelector, sensitivitySelector, weightSelector, heightSelector, genderSelector, instructionsSelector, privacyPolicySelector, languageSelector;
    private PopupWindow popupWindow;
    private ScrollView parentLayout;
    private RadioGroup radioStepGoal, radioGenderSelector;
    private RadioButton radioStepGoalButton, genderRadioButton;
    private NirogyaDataSource nirogyaDataSource;
    private int sensitivity = 0;
    private int weight = 62;
    private int height = 156;
    private TextView stepGoalSelecedValue, sensitivitySelectedValue, weightSelectedValue, heightSelectedValue, genderSelectedValue;
    private String defaultTableRowIndexvalue = "1";

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nirogyaDataSource = new NirogyaDataSource(getActivity());
        nirogyaDataSource.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentSettings = inflater.inflate(R.layout.fragment_settings, container, false);
        // Get the widgets reference from XML layout
        parentLayout = fragmentSettings.findViewById(R.id.fragment_settings);
        stepGoalSelector = fragmentSettings.findViewById(R.id.step_goal_selector);
        sensitivitySelector = fragmentSettings.findViewById(R.id.sensitivity_selector);
        weightSelector = fragmentSettings.findViewById(R.id.weight_selector);
        heightSelector = fragmentSettings.findViewById(R.id.height_selector);
        genderSelector = fragmentSettings.findViewById(R.id.gender_selector);
        instructionsSelector = fragmentSettings.findViewById(R.id.instructions_selector);
        privacyPolicySelector = fragmentSettings.findViewById(R.id.privacy_policy_selector);
        languageSelector = fragmentSettings.findViewById(R.id.language_selector);
        stepGoalSelecedValue = fragmentSettings.findViewById(R.id.step_goal_seleced_value);
        sensitivitySelectedValue = fragmentSettings.findViewById(R.id.sensitivity_seleced_value);
        weightSelectedValue = fragmentSettings.findViewById(R.id.weight_seleced_value);
        heightSelectedValue = fragmentSettings.findViewById(R.id.height_seleced_value);
        genderSelectedValue = fragmentSettings.findViewById(R.id.gender_seleced_value);
        String[] getUserData = nirogyaDataSource.getDataUserDetails(defaultTableRowIndexvalue);
        genderSelectedValue.setText(getUserData[0]);
        heightSelectedValue.setText(getUserData[1]);
        weightSelectedValue.setText(getUserData[2]);
        String[] retriedData = nirogyaDataSource.getAllDataFromPedometer(defaultTableRowIndexvalue);
        sensitivitySelectedValue.setText(retriedData[5]);
        stepGoalSelecedValue.setText(retriedData[4]);
        stepGoalSelector.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                // Inflate the activity_profile_height_selector layout/view
                final View customView = inflater.inflate(R.layout.fragment_settings_step_goal_selector, null);
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
                Display display = getActivity().getWindowManager().getDefaultDisplay();
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
                Button cancelButton = customView.findViewById(R.id.fs_step_goal_cancel_button);
                // Get a reference for the activity_profile_height_selector view OK button
                Button okButton = customView.findViewById(R.id.fs_step_goal_ok_button);
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
                        // get selected radio button from radioGroup
                        int selectedId = radioStepGoal.getCheckedRadioButtonId();
                        // find the radiobutton by returned id
                        radioStepGoalButton = customView.findViewById(selectedId);
                        String radioStepGoalButtonText = radioStepGoalButton.getText().toString();
                        Toast.makeText(getActivity(), radioStepGoalButtonText, Toast.LENGTH_SHORT).show();
                        nirogyaDataSource.updateStepGoalPedomter(defaultTableRowIndexvalue, radioStepGoalButtonText);
                        String[] retriedData = nirogyaDataSource.getAllDataFromPedometer(defaultTableRowIndexvalue);
                        stepGoalSelecedValue.setText(retriedData[4]);
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
                radioStepGoal = customView.findViewById(R.id.radio_step_goal);
            }
        });
        sensitivitySelector.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                // Inflate the activity_profile_height_selector layout/view
                final View customView = inflater.inflate(R.layout.fragment_settings_sensitivity_selector, null);
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
                Display display = getActivity().getWindowManager().getDefaultDisplay();
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
                Button cancelButton = customView.findViewById(R.id.fs_sensitivity_cancel_button);
                // Get a reference for the activity_profile_height_selector view OK button
                Button okButton = customView.findViewById(R.id.fs_sensitivity_ok_button);
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
                        if (sensitivity >= 10) {
                            nirogyaDataSource.updateSensitivityPedomter(defaultTableRowIndexvalue, String.valueOf(sensitivity));
                        }
                        String[] retriedData = nirogyaDataSource.getAllDataFromPedometer(defaultTableRowIndexvalue);
                        sensitivitySelectedValue.setText(retriedData[5]);
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
                SeekBar seekBar = customView.findViewById(R.id.seekBar);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        sensitivity = progress * 10;
                        Toast.makeText(getActivity(), progress + "", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
            }


        });
        weightSelector.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                // Inflate the activity_profile_height_selector layout/view
                View customView = inflater.inflate(R.layout.fragment_settings_weight_selector, null);
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
                Display display = getActivity().getWindowManager().getDefaultDisplay();
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
                Button cancelButton = customView.findViewById(R.id.fs_weight_cancel_button);
                // Get a reference for the activity_profile_height_selector view OK button
                Button okButton = customView.findViewById(R.id.fs_weight_ok_button);
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
                        nirogyaDataSource.updateWeightUserDetails(defaultTableRowIndexvalue, String.valueOf(weight));

                        String[] getUserData = nirogyaDataSource.getDataUserDetails(defaultTableRowIndexvalue);
                        weightSelectedValue.setText(getUserData[2]);
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
                        weight = newVal;
                        //Display the newly selected number from picker
                        Toast.makeText(getActivity(), String.valueOf(newVal), Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });
        heightSelector.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                // Inflate the activity_profile_height_selector layout/view
                View customView = inflater.inflate(R.layout.fragment_settings_height_selector, null);
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
                Display display = getActivity().getWindowManager().getDefaultDisplay();
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
                Button cancelButton = customView.findViewById(R.id.fs_height_cancel_button);
                // Get a reference for the activity_profile_height_selector view OK button
                Button okButton = customView.findViewById(R.id.fs_height_ok_button);
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
                        nirogyaDataSource.updateHeightUserDetails(defaultTableRowIndexvalue, String.valueOf(height));
                        String[] getUserData = nirogyaDataSource.getDataUserDetails(defaultTableRowIndexvalue);
                        heightSelectedValue.setText(getUserData[1]);
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
                        height = newVal;
                        //Display the newly selected number from picker
                        Toast.makeText(getActivity(), String.valueOf(newVal), Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });
        genderSelector.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                // Inflate the activity_profile_height_selector layout/view
                final View customView = inflater.inflate(R.layout.fragment_settings_gender_selector, null);
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
                Display display = getActivity().getWindowManager().getDefaultDisplay();
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
                        // get selected radio button from radioGroup
                        int selectedId = radioGenderSelector.getCheckedRadioButtonId();
                        // find the radiobutton by returned id
                        genderRadioButton = customView.findViewById(selectedId);
                        nirogyaDataSource.updateGenderUserDetails(defaultTableRowIndexvalue, genderRadioButton.getText().toString());
                        Toast.makeText(getActivity(), genderRadioButton.getText(), Toast.LENGTH_SHORT).show();
                        String[] getUserData = nirogyaDataSource.getDataUserDetails(defaultTableRowIndexvalue);
                        genderSelectedValue.setText(getUserData[0]);
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
                radioGenderSelector = customView.findViewById(R.id.radio_gender_selector);
            }


        });
        instructionsSelector.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InstructionsActivity.class);
                startActivity(intent);
            }
        });
        privacyPolicySelector.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });
        languageSelector.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                // Inflate the activity_profile_height_selector layout/view
                final View customView = inflater.inflate(R.layout.fragment_settings_language_selector, null);
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
                Display display = getActivity().getWindowManager().getDefaultDisplay();
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
                Button cancelButton = customView.findViewById(R.id.fs_language_cancel_button);
                // Get a reference for the activity_profile_height_selector view OK button
                Button okButton = customView.findViewById(R.id.fs_language_ok_button);
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
            }
        });
        return fragmentSettings;
    }
}