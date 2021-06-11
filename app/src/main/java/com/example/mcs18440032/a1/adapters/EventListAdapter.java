package com.example.mcs18440032.a1.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcs18440032.a1.R;
import com.example.mcs18440032.a1.ScheduleActivity;
import com.example.mcs18440032.a1.helpers.Helper;
import com.example.mcs18440032.a1.models.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private List<Event> eventList = new ArrayList<>();

    /**
     * Provide a reference to the type of views that we are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cvEvent;
        private final TextView tvEventLocation;
        private final TextView tvEventName;
        private final TextView tvEventDate;
        private final TextView tvStartEndTime;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            cvEvent = (CardView) view.findViewById(R.id.cvEvent);
            tvEventLocation = (TextView) view.findViewById(R.id.tvEventLocation);
            tvEventName = (TextView) view.findViewById(R.id.tvEventName);
            tvEventDate = (TextView) view.findViewById(R.id.tvEventDate);
            tvStartEndTime = (TextView) view.findViewById(R.id.tvStartEndTime);
        }

        public CardView getCvEvent() {
            return cvEvent;
        }

        public TextView getTvEventLocation() {
            return tvEventLocation;
        }

        public TextView getTvEventName() {
            return tvEventName;
        }

        public TextView getTvEventDate() {
            return tvEventDate;
        }

        public TextView getTvStartEndTime() {
            return tvStartEndTime;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param eventList List<Event> containing the data to populate views to be used
     *                  by RecyclerView.
     */
    public EventListAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    /**
     * Set the dataset of the Adapter.
     *
     * @param eventList List<Event> containing the data to populate views to be used
     *                  by RecyclerView.
     */
    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_events_list, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from the dataset at this position and replace the
        // contents of the view with that element
        Event event = eventList.get(position);

        viewHolder.getTvEventName().setText(event.getEventName());
        if (event.getEventLocation().isEmpty() || event.getEventLocation().equals("")) {
            viewHolder.getTvEventLocation().setText(R.string.location_not_available);
        } else {
            viewHolder.getTvEventLocation().setText(String.format("Location : %s", event.getEventLocation()));
        }
        viewHolder.getTvEventDate().setText(String.format("On %s", event.getDate()));

        if (!Objects.requireNonNull(event.getEndTime()).equals("")) {
            viewHolder.getTvStartEndTime().setText(String.format("at %s to %s", event.getStartTime(), event.getEndTime()));
        } else {
            viewHolder.getTvStartEndTime().setText(String.format("at %s", event.getStartTime()));
        }


        viewHolder.getCvEvent().setOnClickListener(view -> {
            Intent eventScheduleIntent = new Intent(view.getContext(), ScheduleActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong(view.getContext().getString(R.string.id), event.getId());
            eventScheduleIntent.putExtras(bundle);
            view.getContext().startActivity(eventScheduleIntent);
        });

    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return eventList.size();
    }
}