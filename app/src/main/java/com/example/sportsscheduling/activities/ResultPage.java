package com.example.sportsscheduling.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sportsscheduling.MainActivity;
import com.example.sportsscheduling.R;
import com.example.sportsscheduling.scheduling.RunAlgorithm;
import com.example.sportsscheduling.scheduling.WriteToFile;

/**
 * The type Result page.
 */
public class ResultPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_result);

        TextView saveInfo = this.findViewById(R.id.saveInfo);
        saveInfo.setVisibility(View.INVISIBLE);

        Button homeButton = this.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHomePage = new Intent();
                toHomePage.setClass(ResultPage.this, MainActivity.class);
                startActivity(toHomePage);
            }
        });

        Button saveButton = this.findViewById(R.id.saveButton_result);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo.setVisibility(View.VISIBLE);
                WriteToFile.createAndWriteExternalFile(ResultPage.this, "schedule.txt", RunAlgorithm.schedulingOutputString());

            }
        });

        RecyclerView recyclerView = this.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ScheduleAdapter(RunAlgorithm.getSchedulingOutput()));

    }

}
