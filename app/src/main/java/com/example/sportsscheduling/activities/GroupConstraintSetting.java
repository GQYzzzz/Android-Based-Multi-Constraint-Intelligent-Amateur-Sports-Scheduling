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
 * The type Group constraint setting.
 */
public class GroupConstraintSetting extends AppCompatActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_constraint);

        TextView error_group = this.findViewById(R.id.errorGroup);
        error_group.setVisibility(View.INVISIBLE);

        Button cancelButton = this.findViewById(R.id.cancelButton_group);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("ConstraintsSettingPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("key_checkbox_group", false); // Modify the state of the checkbox
                editor.apply();

                SharedPreferences prefs_1 = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor_1 = prefs_1.edit();
                editor_1.putString("group_constraint_input", "");
                editor_1.apply();
                editText.setText("");

                Intent toSettingPage = new Intent();
                toSettingPage.setClass(GroupConstraintSetting.this, ConstraintsSetting.class);
                startActivity(toSettingPage);
            }
        });

        editText = findViewById(R.id.inputMaxPerTime);
        Button saveButton = this.findViewById(R.id.saveButton_group);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_group.setVisibility(View.INVISIBLE);

                String text = editText.getText().toString();
                // Invalid if not only containing a number
                if (!text.matches("\\d+")) {
                    error_group.setVisibility(View.VISIBLE);
                } else {
                    SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("group_constraint_input", text);
                    editor.apply();

                    Intent saveInfo = new Intent();
                    saveInfo.setClass(GroupConstraintSetting.this, ConstraintsSetting.class);
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
        editor.putString("group_constraint_input", editText.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restore the input content
        SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
        String savedText = prefs.getString("group_constraint_input", "");
        editText.setText(savedText);
    }

}