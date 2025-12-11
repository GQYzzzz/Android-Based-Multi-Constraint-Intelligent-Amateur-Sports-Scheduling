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
 * The type Date constraint setting.
 */
public class DateConstraintSetting extends AppCompatActivity {
    private EditText editText;
    private String editError = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_constraint);

        TextView error_date = this.findViewById(R.id.errorDate);
        error_date.setVisibility(View.INVISIBLE);

        Button cancelButton = this.findViewById(R.id.cancelButton_date);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("ConstraintsSettingPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("key_checkbox_date", false); // Modify the state of the checkbox
                editor.apply();

                SharedPreferences prefs_1 = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor_1 = prefs_1.edit();
                editor_1.putString("date_constraint_input", "");
                editor_1.apply();
                editText.setText("");

                Intent toSettingPage = new Intent();
                toSettingPage.setClass(DateConstraintSetting.this, ConstraintsSetting.class);
                startActivity(toSettingPage);
            }
        });

        editText = findViewById(R.id.inputDatePreference);
        Button saveButton = this.findViewById(R.id.saveButton_date);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_date.setVisibility(View.INVISIBLE);

                String text = editText.getText().toString();
                SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                String teamNames = prefs.getString("team_names_input", "");
                String[] team_names = teamNames.split(",");
                int duration = Integer.parseInt(prefs.getString("duration_input", ""));
                if (!isValidDateConstraint(text, team_names, duration)) {
                    error_date.setText(editError);
                    error_date.setVisibility(View.VISIBLE);
                } else {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("date_constraint_input", text);
                    editor.apply();

                    Intent saveInfo = new Intent();
                    saveInfo.setClass(DateConstraintSetting.this, ConstraintsSetting.class);
                    startActivity(saveInfo);
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
        editor.putString("date_constraint_input", editText.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restore the input content
        SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
        String savedText = prefs.getString("date_constraint_input", "");
        editText.setText(savedText);
    }

    /**
     * Is valid date constraint boolean.
     *
     * @param input     the input
     * @param teamNames the team names
     * @param duration  the duration
     * @return the boolean
     */
    public boolean isValidDateConstraint(String input, String[] teamNames, int duration) {
        // Invalid if the content is empty
        if (input == null || input.trim().isEmpty()) {
            editError = "Input cannot be empty!";
            return false;
        }

        String[] constraints = input.trim().split(";");
        int count = 0;
        for (String constraint : constraints) {
            count++;
            String[] parts = constraint.trim().split(",");

            // Invalid if "parts" does not contain 4 items：team1,team2,operator,day
            if (parts.length != 4) {
                editError = "Each preference must have 4 parts, the preference no." + count + " only has " + parts.length+" parts!";
                return false;
            }

            String team1 = parts[0].trim();
            String team2 = parts[1].trim();
            String operator = parts[2].trim();
            String dayStr = parts[3].trim();

            // Invalid if the team name is not in the team list
            if (!contains(teamNames, team1)) {
                editError = "Team name " + team1 + " is not in the team list!";
                return false;
            }
            if (!contains(teamNames, team2)) {
                editError = "Team name " + team2 + " is not in the team list!";
                return false;
            }

            // Invalid if the operator is not "<=" or ">="
            if (!operator.equals("<=") && !operator.equals(">=")) {
                editError = "The operator " + operator + " is incorrect!";
                return false;
            }

            // Invalid if the day number is not a valid number or exceed duration
            int day;
            try {
                day = Integer.parseInt(dayStr);
            } catch (NumberFormatException e) {
                editError = "The day number is not a valid number or exceed duration!";
                return false;
            }

            if (day < 1 || day > duration) {
                editError = "The day number " + day + " is less than 1 or exceed duration!";
                return false;
            }
        }

        return true;
    }

    private boolean contains(String[] array, String target) {
        for (String s : array) {
            if (s.trim().equals(target)) return true;
        }
        return false;
    }

}