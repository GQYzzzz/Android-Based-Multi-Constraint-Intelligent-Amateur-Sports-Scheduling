package com.example.sportsscheduling.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsscheduling.R;

import java.util.List;

/**
 * The type Schedule adapter.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private List<String> scheduleList;

    /**
     * Instantiates a new Schedule adapter.
     *
     * @param scheduleList the schedule list
     */
    public ScheduleAdapter(List<String> scheduleList) {
        this.scheduleList = scheduleList;
    }

    /**
     * The type View holder.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Schedule text.
         */
        public TextView scheduleText;

        /**
         * Instantiates a new View holder.
         *
         * @param view the view
         */
        public ViewHolder(View view) {
            super(view);
            scheduleText = view.findViewById(R.id.scheduleText);
        }
    }

    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.scheduleText.setText(scheduleList.get(position));
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }
}