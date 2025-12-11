package com.example.sportsscheduling.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sportsscheduling.R;

/**
 * The type Constraints setting.
 */
public class ConstraintsSetting extends AppCompatActivity {
    private static final String KEY_CHECKBOX_GROUP = "key_checkbox_group";
    private static final String KEY_CHECKBOX_DATE = "key_checkbox_date";
    private static final String KEY_CHECKBOX_SEPARATION = "key_checkbox_separation";
    private static final String KEY_CHECKBOX_PREFERENCE = "key_checkbox_preference";
    private static final String KEY_CHECKBOX_REST = "key_checkbox_rest";
    private static final String KEY_CHECKBOX_DAILY = "key_checkbox_daily";
    private static final String PREFS_NAME = "ConstraintsSettingPrefs";
    private CheckBox checkGroup;
    private CheckBox checkDate;
    private CheckBox checkSeparation;
    private CheckBox checkTeamPreference;
    private CheckBox checkRestDifference;
    private CheckBox checkDaily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constraints_setting);

        checkGroup = findViewById(R.id.checkboxGroupConstraint);
        // Restore the state of the checkbox
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean checked_group = preferences.getBoolean(KEY_CHECKBOX_GROUP, false);
        checkGroup.setChecked(checked_group);

        checkGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if (checked) {
                    Intent setMaxPerDay = new Intent();
                    setMaxPerDay.setClass(ConstraintsSetting.this, GroupConstraintSetting.class);
                    startActivity(setMaxPerDay);
                }
            }
        });

        checkDate = findViewById(R.id.checkboxDateConstraint);
        // Restore the state of the checkbox
        boolean checked_date = preferences.getBoolean(KEY_CHECKBOX_DATE, false);
        checkDate.setChecked(checked_date);

        checkDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if (checked) {
                    Intent setMaxPerDay = new Intent();
                    setMaxPerDay.setClass(ConstraintsSetting.this, DateConstraintSetting.class);
                    startActivity(setMaxPerDay);
                }
            }
        });

        checkSeparation = findViewById(R.id.checkboxSeparationConstraint);
        // Restore the state of the checkbox
        boolean checked_separation = preferences.getBoolean(KEY_CHECKBOX_SEPARATION, false);
        checkSeparation.setChecked(checked_separation);

        checkSeparation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if (checked) {
                    Intent setMaxConsecutiveDays = new Intent();
                    setMaxConsecutiveDays.setClass(ConstraintsSetting.this, SeparationConstraintSetting.class);
                    startActivity(setMaxConsecutiveDays);
                }
            }
        });

        checkTeamPreference = findViewById(R.id.checkboxPreferenceConstraint);
        // Restore the state of the checkbox
        boolean checked_preference = preferences.getBoolean(KEY_CHECKBOX_PREFERENCE, false);
        checkTeamPreference.setChecked(checked_preference);

        checkTeamPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if (checked) {
                    Intent setPreference = new Intent();
                    setPreference.setClass(ConstraintsSetting.this, TeamPreferenceConstraintSetting.class);
                    startActivity(setPreference);
                }
            }
        });

        checkRestDifference = findViewById(R.id.checkboxRestConstraint);
        // Restore the state of the checkbox
        boolean checked_rest = preferences.getBoolean(KEY_CHECKBOX_REST, false);
        checkRestDifference.setChecked(checked_rest);

        checkDaily = findViewById(R.id.checkboxDailyConstraint);
        // Restore the state of the checkbox
        boolean checked_daily = preferences.getBoolean(KEY_CHECKBOX_DAILY, false);
        checkDaily.setChecked(checked_daily);

        Button saveButton = this.findViewById(R.id.saveButton_constraints);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent saveInfo = new Intent();
                saveInfo.setClass(ConstraintsSetting.this, SettingPage.class);
                startActivity(saveInfo);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save the state of the CheckBox to SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_CHECKBOX_GROUP, checkGroup.isChecked());
        editor.putBoolean(KEY_CHECKBOX_DATE, checkDate.isChecked());
        editor.putBoolean(KEY_CHECKBOX_SEPARATION, checkSeparation.isChecked());
        editor.putBoolean(KEY_CHECKBOX_PREFERENCE, checkTeamPreference.isChecked());
        editor.putBoolean(KEY_CHECKBOX_REST, checkRestDifference.isChecked());
        editor.putBoolean(KEY_CHECKBOX_DAILY, checkDaily.isChecked());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restore the state of the CheckBox from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean checked_group = preferences.getBoolean(KEY_CHECKBOX_GROUP, false);
        checkGroup.setChecked(checked_group);
        boolean checked_date = preferences.getBoolean(KEY_CHECKBOX_DATE, false);
        checkDate.setChecked(checked_date);
        boolean checked_separation = preferences.getBoolean(KEY_CHECKBOX_SEPARATION, false);
        checkSeparation.setChecked(checked_separation);
        boolean checked_preference = preferences.getBoolean(KEY_CHECKBOX_PREFERENCE, false);
        checkTeamPreference.setChecked(checked_preference);
        boolean checked_rest = preferences.getBoolean(KEY_CHECKBOX_REST, false);
        checkRestDifference.setChecked(checked_rest);
        boolean checked_daily = preferences.getBoolean(KEY_CHECKBOX_DAILY, false);
        checkDaily.setChecked(checked_daily);
    }

}