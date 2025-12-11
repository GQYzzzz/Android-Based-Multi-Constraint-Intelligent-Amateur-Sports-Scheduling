package com.example.sportsscheduling.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sportsscheduling.R;

/**
 * The type Time setting.
 */
public class TimeSetting extends AppCompatActivity {
    private EditText editText_duration;
    private EditText editText_timeSlots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_setting);

        TextView error_time_format = this.findViewById(R.id.errorTimeSlotsFormat);
        TextView error_duration_min = this.findViewById(R.id.errorMinDuration);
        TextView error_duration_invalid = this.findViewById(R.id.errorDuration);
        TextView error_save = this.findViewById(R.id.errorSave);
        error_time_format.setVisibility(View.INVISIBLE);
        error_duration_min.setVisibility(View.INVISIBLE);
        error_duration_invalid.setVisibility(View.INVISIBLE);
        error_save.setVisibility(View.INVISIBLE);

        Button cancelButton = this.findViewById(R.id.cancelButton_time);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs_1 = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor_1 = prefs_1.edit();
                editor_1.putString("duration_input", "");
                editor_1.putString("timeSlots_input", "");
                editor_1.putString("time_finish", "");
                editor_1.apply();
                editText_duration.setText("");
                editText_timeSlots.setText("");

                Intent toSettingPage = new Intent();
                toSettingPage.setClass(TimeSetting.this, SettingPage.class);
                startActivity(toSettingPage);
            }
        });

        editText_duration = findViewById(R.id.inputDuration);
        editText_timeSlots = findViewById(R.id.inputTimeSlots);
        Button saveButton = this.findViewById(R.id.saveButton_time);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_time_format.setVisibility(View.INVISIBLE);
                error_duration_min.setVisibility(View.INVISIBLE);
                error_duration_invalid.setVisibility(View.INVISIBLE);
                error_save.setVisibility(View.INVISIBLE);

                String text_duration = editText_duration.getText().toString();
                String text_timeSlots = editText_timeSlots.getText().toString();

                if(text_duration.isEmpty() || text_timeSlots.isEmpty()){
                    error_save.setVisibility(View.VISIBLE);
                }
                else{
                    boolean isValid = true;
                    if(!isValidTimeSlots(text_timeSlots)){
                        error_time_format.setVisibility(View.VISIBLE);
                        isValid = false;
                    }
                    if (!text_duration.matches("\\d+")) {
                        // Only numbers
                        error_duration_invalid.setVisibility(View.VISIBLE);
                        isValid = false;
                    }
                    if (isValid && !isValidDuration(text_duration)){
                        error_duration_min.setVisibility(View.VISIBLE);
                        isValid = false;
                    }
                    if (isValid) {
                        SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("duration_input", text_duration);
                        editor.putString("timeSlots_input", text_timeSlots);
                        editor.putString("time_finish", "true");
                        editor.apply();

                        Intent saveInfo = new Intent();
                        saveInfo.setClass(TimeSetting.this, SettingPage.class);
                        startActivity(saveInfo);
                    }
                }

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save the input content
        SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("duration_input", editText_duration.getText().toString());
        editor.putString("timeSlots_input", editText_timeSlots.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restore the input content
        SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
        String savedText_duration = prefs.getString("duration_input", "");
        editText_duration.setText(savedText_duration);
        String savedText_timeSlots = prefs.getString("timeSlots_input", "");
        editText_timeSlots.setText(savedText_timeSlots);
    }

    /**
     * Is valid time slots boolean.
     *
     * @param input the input
     * @return the boolean
     */
    public boolean isValidTimeSlots(String input) {
        // e.g. 09am,11am,01pm,03pm
        String pattern = "^((0[1-9]|1[0-2])(am|pm))(,\\s*(0[1-9]|1[0-2])(am|pm))*$";
        return input.matches(pattern);
    }

    /**
     * Is valid duration boolean.
     *
     * @param input the input
     * @return the boolean
     */
    public boolean isValidDuration(String input) {
        int duration = Integer.parseInt(input);
        SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
        String teamNames = prefs.getString("team_names_input", "");
        int n =teamNames.split(",").length;
        String venueNames = prefs.getString("venue_names_input", "");
        int v = venueNames.split(",").length;
        int t = editText_timeSlots.getText().toString().split(",").length;

        if(duration >= (n*(n-1)/(2*v*t))){
            return true;
        }

        return false;
    }

}
