package com.example.sportsscheduling;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sportsscheduling.activities.SettingPage;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        long startTime = System.currentTimeMillis();
        //Log.d(TAG, RunAlgorithm.runPilot());
        //Log.d(TAG, RunAlgorithm.run());
        //WriteToFile.createAndWriteExternalFile(this, "cost.txt", RunAlgorithm.getOutputCost());
        long endTime = System.currentTimeMillis();
        Log.d(TAG, "run time: " + (endTime - startTime) + "ms");

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialise the state of all checkboxes
        SharedPreferences prefs = getSharedPreferences("ConstraintsSettingPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        SharedPreferences prefs_1 = getSharedPreferences("InputPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor_1 = prefs_1.edit();
        editor_1.clear();
        editor_1.apply();

        Button startButton = this.findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSettingPage = new Intent();
                toSettingPage.setClass(MainActivity.this, SettingPage.class);
                startActivity(toSettingPage);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}