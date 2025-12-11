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
 * The type Separation constraint setting.
 */
public class SeparationConstraintSetting extends AppCompatActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.separation_constraint);

        TextView error_separation = this.findViewById(R.id.errorSeparation);
        error_separation.setVisibility(View.INVISIBLE);

        Button cancelButton = this.findViewById(R.id.cancelButton_separation);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("ConstraintsSettingPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("key_checkbox_separation", false); // Modify the state of the checkbox
                editor.apply();

                SharedPreferences prefs_1 = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor_1 = prefs_1.edit();
                editor_1.putString("separation_constraint_input", "");
                editor_1.apply();
                editText.setText("");

                Intent toSettingPage = new Intent();
                toSettingPage.setClass(SeparationConstraintSetting.this, ConstraintsSetting.class);
                startActivity(toSettingPage);
            }
        });

        editText = findViewById(R.id.inputMaxConsecutiveDays);
        Button saveButton = this.findViewById(R.id.saveButton_separation);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_separation.setVisibility(View.INVISIBLE);

                String text = editText.getText().toString();
                // Invalid if containing not only a number
                if (!text.matches("\\d+")) {
                    error_separation.setVisibility(View.VISIBLE);
                } else {
                    SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("separation_constraint_input", text);
                    editor.apply();

                    Intent saveInfo = new Intent();
                    saveInfo.setClass(SeparationConstraintSetting.this, ConstraintsSetting.class);
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
        editor.putString("separation_constraint_input", editText.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restore the input content
        SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
        String savedText = prefs.getString("separation_constraint_input", "");
        editText.setText(savedText);
    }

}
