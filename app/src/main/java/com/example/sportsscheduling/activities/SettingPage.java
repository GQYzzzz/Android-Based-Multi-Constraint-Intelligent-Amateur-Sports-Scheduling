package com.example.sportsscheduling.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sportsscheduling.MainActivity;
import com.example.sportsscheduling.R;
import com.example.sportsscheduling.scheduling.RunAlgorithm;
import com.example.sportsscheduling.scheduling.Team;
import com.example.sportsscheduling.scheduling.Venue;

import java.util.*;

/**
 * The type Setting page.
 */
public class SettingPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page);

        TextView error_constraint = this.findViewById(R.id.errorConstraint);
        TextView error_generate = this.findViewById(R.id.errorGenerate);
        TextView error_time = this.findViewById(R.id.errorTime);
        TextView start_generate = this.findViewById(R.id.startGenerate);
        error_constraint.setVisibility(View.INVISIBLE);
        error_generate.setVisibility(View.INVISIBLE);
        error_time.setVisibility(View.INVISIBLE);
        start_generate.setVisibility(View.INVISIBLE);

        Button teamSettingButton = this.findViewById(R.id.teamSettingButton);
        teamSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toTeamSettingPage = new Intent();
                toTeamSettingPage.setClass(SettingPage.this, TeamSetting.class);
                startActivity(toTeamSettingPage);
            }
        });

        Button venueSettingButton = this.findViewById(R.id.venueSettingButton);
        venueSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toVenueSettingPage = new Intent();
                toVenueSettingPage.setClass(SettingPage.this, VenueSetting.class);
                startActivity(toVenueSettingPage);
            }
        });

        Button timeSettingButton = this.findViewById(R.id.timeSettingButton);
        timeSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_constraint.setVisibility(View.INVISIBLE);
                error_generate.setVisibility(View.INVISIBLE);
                error_time.setVisibility(View.INVISIBLE);
                SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                String team = prefs.getString("team_finish", "");
                String venue = prefs.getString("venue_finish", "");
                if (!team.equals("true") || !venue.equals("true")) {
                    error_time.setVisibility(View.VISIBLE);
                } else {
                    Intent toTimeSettingPage = new Intent();
                    toTimeSettingPage.setClass(SettingPage.this, TimeSetting.class);
                    startActivity(toTimeSettingPage);
                }
            }
        });

        Button constraintsSettingButton = this.findViewById(R.id.constraintsSettingButton);
        constraintsSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_constraint.setVisibility(View.INVISIBLE);
                error_generate.setVisibility(View.INVISIBLE);
                error_time.setVisibility(View.INVISIBLE);

                SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                String team = prefs.getString("team_finish", "");
                String venue = prefs.getString("venue_finish", "");
                String time = prefs.getString("time_finish", "");
                if (!team.equals("true") || !venue.equals("true") || !time.equals("true")) {
                    error_constraint.setVisibility(View.VISIBLE);
                } else {
                    Intent toConstraintsSettingPage = new Intent();
                    toConstraintsSettingPage.setClass(SettingPage.this, ConstraintsSetting.class);
                    startActivity(toConstraintsSettingPage);
                }
            }
        });

        Button cancelButton = this.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMainPage = new Intent();
                toMainPage.setClass(SettingPage.this, MainActivity.class);
                startActivity(toMainPage);
            }
        });

        Button generateButton = this.findViewById(R.id.generateButton);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_constraint.setVisibility(View.INVISIBLE);
                error_generate.setVisibility(View.INVISIBLE);
                error_time.setVisibility(View.INVISIBLE);
                start_generate.setVisibility(View.INVISIBLE);

                SharedPreferences prefs = getSharedPreferences("InputPrefs", MODE_PRIVATE);
                String team_finish = prefs.getString("team_finish", "");
                String venue_finish = prefs.getString("venue_finish", "");
                String time_finish = prefs.getString("time_finish", "");
                if (!team_finish.equals("true") || !venue_finish.equals("true") || !time_finish.equals("true")) {
                    error_generate.setVisibility(View.VISIBLE);
                } else {
                    start_generate.setVisibility(View.VISIBLE);

                    Log.d("MainActivity", "click");

                    boolean isGroupConstraint = false;
                    boolean isDateConstraint = false;
                    boolean isSeperationConstraint = false;
                    boolean isRestDifferenceConstraint = false;
                    boolean isDailyConstraint = false;
                    boolean isPreferenceConstraint = false;
                    int maxPerTime = 0;
                    String datePreference = "";
                    int separationDay = 0;

                    // Set team information and preference
                    String teamNamesString = prefs.getString("team_names_input", "");
                    List<Team> teams = new ArrayList<>();
                    String[] teamNames = teamNamesString.split(",");
                    for (String name : teamNames) {
                        // Remove any space
                        String trimmedName = name.trim();
                        Team t = new Team(trimmedName, "", "");
                        teams.add(t);
                    }

                    SharedPreferences prefs_con = getSharedPreferences("ConstraintsSettingPrefs", MODE_PRIVATE);
                    boolean isCheckedTeamPreference = prefs_con.getBoolean("key_checkbox_preference", false);
                    // If having team preference, then update the team list
                    if (isCheckedTeamPreference) {
                        isPreferenceConstraint = true;
                        String teamPreference = prefs.getString("preference_constraint_input", "");

                        String[] prefArray = teamPreference.split(";");
                        Map<String, String[]> preferenceMap = new HashMap<>();
                        for (String pref : prefArray) {
                            String[] parts = pref.split(",");
                            if (parts.length >= 3) {
                                String teamName = parts[0].trim();
                                String venuePref = parts[1].trim();
                                String timePref = parts[2].trim();
                                preferenceMap.put(teamName, new String[]{venuePref, timePref});
                            }
                        }

                        for (Team team : teams) {
                            if (preferenceMap.containsKey(team.getName())) {
                                String[] pref = preferenceMap.get(team.getName());
                                team.setPreferredVenue(pref[0]);
                                team.setPreferredTime(pref[1]);
                            }
                        }
                    }

                    // Set venue information
                    List<Venue> venues = new ArrayList<>();
                    String venueNamesString = prefs.getString("venue_names_input", "");
                    String[] venueNames = venueNamesString.split(",");
                    for (String venue : venueNames) {
                        // Remove any space
                        String trimmedName = venue.trim();
                        Venue newVenue = new Venue(trimmedName);
                        venues.add(newVenue);
                    }

                    // Set time information
                    String duration = prefs.getString("duration_input", "");
                    int totalDays = Integer.parseInt(duration);

                    List<String> times = new ArrayList<>();
                    String timeSlotsString = prefs.getString("timeSlots_input", "");
                    String[] timeSlots = timeSlotsString.split(",");
                    for (String time : timeSlots) {
                        // Remove any space
                        String trimmedTime = time.trim();
                        times.add(trimmedTime);
                    }

                    // Set constraints
                    // Set group constraint
                    boolean isCheckedGroupConstraint = prefs_con.getBoolean("key_checkbox_group", false);
                    if (isCheckedGroupConstraint) {
                        isGroupConstraint = true;
                        maxPerTime = Integer.parseInt(prefs.getString("group_constraint_input", ""));
                    }

                    // Set date constraint
                    boolean isCheckedDateConstraint = prefs_con.getBoolean("key_checkbox_date", false);
                    if (isCheckedDateConstraint) {
                        isDateConstraint = true;
                        datePreference = prefs.getString("date_constraint_input", "");
                    }

                    // Set separation constraint
                    boolean isCheckedSeparationConstraint = prefs_con.getBoolean("key_checkbox_separation", false);
                    if (isCheckedSeparationConstraint) {
                        isSeperationConstraint = true;
                        separationDay = Integer.parseInt(prefs.getString("separation_constraint_input", ""));
                    }

                    // Set rest difference constraint
                    boolean isCheckedRestConstraint = prefs_con.getBoolean("key_checkbox_rest", false);
                    if (isCheckedRestConstraint) {
                        isRestDifferenceConstraint = true;
                    }

                    // Set daily constraint
                    int maxDaily = 1;
                    boolean isCheckedDailyConstraint = prefs_con.getBoolean("key_checkbox_daily", false);
                    if (isCheckedDailyConstraint) {
                        isDailyConstraint = true;
                    }

                    Log.d("MainActivity",
                            RunAlgorithm.runScheduler(
                                    teams, venues, times,
                                    isGroupConstraint, isDateConstraint, isSeperationConstraint,
                                    isRestDifferenceConstraint, isDailyConstraint, isPreferenceConstraint,
                                    totalDays, maxDaily, maxPerTime, datePreference, separationDay)
                    );


                    Intent generateResult = new Intent();
                    generateResult.setClass(SettingPage.this, ResultPage.class);
                    startActivity(generateResult);
                }
            }
        });

    }

}
