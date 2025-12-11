package com.example.sportsscheduling.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sportsscheduling.MainActivity;
import com.example.sportsscheduling.R;

/**
 * The type Team setting.
 */
public class TeamSetting extends AppCompatActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_setting);

        TextView errorTeam = this.findViewById(R.id.errorTeam);
        errorTeam.setVisibility(View.INVISIBLE);

        Button cancelButton = this.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs_1 = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor_1 = prefs_1.edit();
                editor_1.putString("team_names_input", "");
                editor_1.putString("team_finish", "");
                editor_1.apply();
                editText.setText("");

                Intent toSettingPage = new Intent();
                toSettingPage.setClass(TeamSetting.this, SettingPage.class);
                startActivity(toSettingPage);
            }
        });

        editText = findViewById(R.id.inputTeamNames);
        Button saveButton = this.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorTeam.setVisibility(View.INVISIBLE);

                String text = editText.getText().toString();
                if(!isValidTeamInput(text)){
                    errorTeam.setVisibility(View.VISIBLE);
                } else {
                    SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("team_names_input", text);
                    editor.apply();

                    Intent saveInfo = new Intent();
                    saveInfo.setClass(TeamSetting.this, SettingPage.class);
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
        editor.putString("team_names_input", editText.getText().toString());
        editor.putString("team_finish", "true");
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restore the input content
        SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
        String savedText = prefs.getString("team_names_input", "");
        editText.setText(savedText);
    }

    /**
     * Is valid team input boolean.
     *
     * @param input the input
     * @return the boolean
     */
    public boolean isValidTeamInput(String input) {
        // Invalid if null or only spaces
        if (input == null || input.trim().isEmpty()) return false;

        String[] teams = input.trim().split(",", -1);
        if(teams.length == 0) return false;

        // Invalid if containing empty team name
        for (String team : teams) {
            String name = team.trim();
            if (name.isEmpty()) return false;
        }

        return true;
    }

}
