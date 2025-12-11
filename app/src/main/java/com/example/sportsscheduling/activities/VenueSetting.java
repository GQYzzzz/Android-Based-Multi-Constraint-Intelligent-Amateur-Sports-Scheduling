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
 * The type Venue setting.
 */
public class VenueSetting extends AppCompatActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venue_setting);

        TextView errorVenue = this.findViewById(R.id.errorVenue);
        errorVenue.setVisibility(View.INVISIBLE);

        Button cancelButton = this.findViewById(R.id.cancelButton_venue);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs_1 = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor_1 = prefs_1.edit();
                editor_1.putString("venue_names_input", "");
                editor_1.putString("venue_finish", "");
                editor_1.apply();
                editText.setText("");

                Intent toSettingPage = new Intent();
                toSettingPage.setClass(VenueSetting.this, SettingPage.class);
                startActivity(toSettingPage);
            }
        });

        editText = findViewById(R.id.inputVenueNames);
        Button saveButton = this.findViewById(R.id.saveButton_venue);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorVenue.setVisibility(View.INVISIBLE);

                String text = editText.getText().toString();
                if(!isValidVenueInput(text)){
                    errorVenue.setVisibility(View.VISIBLE);
                } else {
                    SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("venue_names_input", text);
                    editor.putString("venue_finish", "true");
                    editor.apply();

                    Intent saveInfo = new Intent();
                    saveInfo.setClass(VenueSetting.this, SettingPage.class);
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
        editor.putString("venue_names_input", editText.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restore the input content
        SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
        String savedText = prefs.getString("venue_names_input", "");
        editText.setText(savedText);
    }

    /**
     * Is valid venue input boolean.
     *
     * @param input the input
     * @return the boolean
     */
    public boolean isValidVenueInput(String input) {
        // Invalid if null or only spaces
        if (input == null || input.trim().isEmpty()) return false;

        String[] venues = input.trim().split(",", -1);
        if(venues.length == 0) return false;

        // Invalid if containing empty venue name
        for (String venue : venues) {
            String name = venue.trim();
            if (name.isEmpty()) return false;
        }

        return true;
    }

}
