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
 * The type Team preference constraint setting.
 */
public class TeamPreferenceConstraintSetting extends AppCompatActivity {
    private EditText editText;
    private String editError = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_preference_constraint);

        TextView error_preference = this.findViewById(R.id.errorPreference);
        error_preference.setVisibility(View.INVISIBLE);

        Button cancelButton = this.findViewById(R.id.cancelButton_preference);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("ConstraintsSettingPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("key_checkbox_preference", false); // Modify the state of the checkbox
                editor.apply();

                SharedPreferences prefs_1 = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor_1 = prefs_1.edit();
                editor_1.putString("preference_constraint_input", "");
                editor_1.apply();
                editText.setText("");

                Intent toSettingPage = new Intent();
                toSettingPage.setClass(TeamPreferenceConstraintSetting.this, ConstraintsSetting.class);
                startActivity(toSettingPage);
            }
        });

        editText = findViewById(R.id.inputTeamPreference);
        Button saveButton = this.findViewById(R.id.saveButton_preference);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_preference.setVisibility(View.INVISIBLE);

                String text = editText.getText().toString();
                SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                String teamNames = prefs.getString("team_names_input", "");
                String[] team_names = teamNames.split(",");
                String venueNames = prefs.getString("venue_names_input", "");
                String[] venue_names = venueNames.split(",");
                String timeSlots = prefs.getString("timeSlots_input", "");
                String[] time_slots = timeSlots.split(",");
                if(!isValidTeamPreference(text, team_names, venue_names, time_slots)){
                    error_preference.setText(editError);
                    error_preference.setVisibility(View.VISIBLE);
                } else {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("preference_constraint_input", text);
                    editor.apply();

                    Intent saveInfo = new Intent();
                    saveInfo.setClass(TeamPreferenceConstraintSetting.this, ConstraintsSetting.class);
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
        editor.putString("preference_constraint_input", editText.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restore the input content
        SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
        String savedText = prefs.getString("preference_constraint_input", "");
        editText.setText(savedText);
    }

    /**
     * Is valid team preference boolean.
     *
     * @param input      the input
     * @param teamNames  the team names
     * @param venueNames the venue names
     * @param timeSlots  the time slots
     * @return the boolean
     */
    public boolean isValidTeamPreference(String input, String[] teamNames, String[] venueNames, String[] timeSlots) {
        // Invalid if the input is empty
        if (input == null || input.trim().isEmpty()){
            editError = "Input cannot be empty!";
            return false;
        }

        String[] preferences = input.trim().split(";");
        int count = 0;
        for (String pref : preferences) {
            count++;
            String[] parts = pref.trim().split(",",-1);

            // Each item must have 3 parts (team name, venue, time)
            if (parts.length != 3) {
                editError = "Each preference must have 3 parts, the preference no." + count + " only has " + parts.length+" parts!";
                return false;
            }

            String team = parts[0].trim();
            String venue = parts[1].trim();
            String time = parts[2].trim();

            // Invalid if the team name is not in the team list
            if (!contains(teamNames, team)) {
                editError = "Team name " + team + " is not in the team list!";
                return false;
            }

            // Invalid if the venue name is not in the venue list (a space is allowed if no venue preference)
            if (!venue.trim().isEmpty() && !contains(venueNames, venue)) {
                editError = "The venue preference of team " + team + " is not in the venue list!";
                return false;
            }

            // Invalid if the time is not in the time slots list (a space is allowed if no time preference)
            if (!time.trim().isEmpty() && !contains(timeSlots, time)) {
                editError = "The time preference of team " + team + " is not in the time list!";
                return false;
            }

            // Venue and time slots could not both be the space at the same time
            if (venue.trim().isEmpty() && time.trim().isEmpty()) {
                editError = "The venue and time preference of team " + team + " is both empty!";
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