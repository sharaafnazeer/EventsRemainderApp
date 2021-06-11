package com.example.mcs18440032.a1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.mcs18440032.a1.db.event.EventEntity;
import com.example.mcs18440032.a1.db.SharedPref;
import com.example.mcs18440032.a1.helpers.Helper;
import com.example.mcs18440032.a1.models.Event;
import com.google.android.material.textfield.TextInputEditText;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

public class ScheduleActivity extends AppCompatActivity implements View.OnFocusChangeListener,
        View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final static String START = "START";
    private final static String END = "END";
    private final static String REMAINDER_1 = "REMAINDER_1";
    private final static String REMAINDER_2 = "REMAINDER_2";
    private final static String REMAINDER_3 = "REMAINDER_3";

    private TextInputEditText etEventName;
    private TextInputEditText etEventLocation;
    private TextInputEditText etEventDescription;
    private TextInputEditText etStartTime;
    private TextInputEditText etEndTime;

    private Spinner spRemainder1;
    private Spinner spRemainder2;
    private Spinner spRemainder3;
    private CheckBox cbRemainder1;
    private CheckBox cbRemainder2;
    private CheckBox cbRemainder3;

    private Button btnAddEdit;
    private Button btnDelete;

    private EventEntity eventEntity;
    private String eventDate;
    private long eventId = 0;
    private String[] remainderArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        initializeDataEntity(); // Setting the event entity with context
        initializeFields(); // Initialize activity fields
        initializeListeners(); // Initialize onClick and onFocus listeners
        setTitleForActivity(); // Setting the title for the activity

        if (eventId > 0) {
            // Event exists; so need to get the values and display in the available fields
            getEventById();
        }
    }

    private void setTitleForActivity() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(eventId > 0 ? getString(R.string.update_event) : getString(R.string.add_event));
    }

    private void initializeListeners() {
        // On focus listener for start time and end time text fields
        etStartTime.setOnFocusChangeListener(this);
        etEndTime.setOnFocusChangeListener(this);

        // On click listener for start time and end time text fields; to handle repeated clicks on the text field
        etStartTime.setOnClickListener(this);
        etEndTime.setOnClickListener(this);

        // On click listener for add and delete buttons
        btnAddEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        spRemainder1.setOnItemSelectedListener(this);
        spRemainder2.setOnItemSelectedListener(this);
        spRemainder3.setOnItemSelectedListener(this);

        cbRemainder1.setOnClickListener(this);
        cbRemainder2.setOnClickListener(this);
        cbRemainder3.setOnClickListener(this);
    }

    private void initializeFields() {
        // Get bundle values form previous activity
        Bundle bundle = getIntent().getExtras();
        eventDate = bundle.getString(getString(R.string.date), null);
        eventId = bundle.getLong(getString(R.string.id), 0);

        // Get remainder related array from string resource
        remainderArray = getResources().getStringArray(R.array.array_remainder);

        etStartTime = findViewById(R.id.etStartTime);
        etEndTime = findViewById(R.id.etEndTime);
        etEventName = findViewById(R.id.etEventName);
        etEventLocation = findViewById(R.id.etEventPlace);
        etEventDescription = findViewById(R.id.etDescription);
        btnAddEdit = findViewById(R.id.btnAddEditEvent);
        btnDelete = findViewById(R.id.btnDeleteEvent);
        spRemainder1 = findViewById(R.id.spRemainder1);
        spRemainder2 = findViewById(R.id.spRemainder2);
        spRemainder3 = findViewById(R.id.spRemainder3);
        cbRemainder1 = findViewById(R.id.cbRemainder1);
        cbRemainder2 = findViewById(R.id.cbRemainder2);
        cbRemainder3 = findViewById(R.id.cbRemainder3);

        // Setting the remainder values to an adapter in order to display inside the spinners
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, remainderArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spRemainder1.setAdapter(adapter);
        spRemainder2.setAdapter(adapter);
        spRemainder3.setAdapter(adapter);

        btnAddEdit.setText(eventId > 0 ? getString(R.string.update_event) :
                getString(R.string.add_event));
        btnDelete.setVisibility(eventId > 0 ? View.VISIBLE : View.INVISIBLE);

        cbRemainder1.setChecked(true);
        cbRemainder2.setChecked(false);
        cbRemainder3.setChecked(false);

        spRemainder1.setVisibility(View.VISIBLE);
        spRemainder2.setVisibility(View.INVISIBLE);
        spRemainder3.setVisibility(View.INVISIBLE);

    }

    private void initializeDataEntity() {
        eventEntity = new EventEntity(getApplicationContext());
    }

    private void getEventById() {
        Event event = eventEntity.get(eventId);
        etEventName.setText(event.getEventName());
        etEventLocation.setText(event.getEventLocation());
        etEventDescription.setText(event.getDescription());
        etStartTime.setText(event.getStartTime());
        etEndTime.setText(event.getEndTime());

        eventDate = event.getDate();

        if (event.getRemainder1() != null) {
            spRemainder1.setVisibility(View.VISIBLE);
            cbRemainder1.setChecked(true);
            spRemainder1.setSelection(Arrays.asList(remainderArray)
                    .indexOf(Helper.convertRemainderToUiFormat(event.getRemainder1())));
        } else {
            spRemainder1.setVisibility(View.INVISIBLE);
            cbRemainder1.setChecked(false);
        }
        if (event.getRemainder2() != null) {
            spRemainder2.setVisibility(View.VISIBLE);
            cbRemainder2.setChecked(true);
            spRemainder2.setSelection(Arrays.asList(remainderArray)
                    .indexOf(Helper.convertRemainderToUiFormat(event.getRemainder2())));
        } else {
            spRemainder2.setVisibility(View.INVISIBLE);
            cbRemainder2.setChecked(false);
        }
        if (event.getRemainder3() != null) {
            spRemainder3.setVisibility(View.VISIBLE);
            cbRemainder3.setChecked(true);
            spRemainder3.setSelection(Arrays.asList(remainderArray)
                    .indexOf(Helper.convertRemainderToUiFormat(event.getRemainder3())));
        } else {
            spRemainder3.setVisibility(View.INVISIBLE);
            cbRemainder3.setChecked(false);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            switch (view.getId()) {
                case R.id.etStartTime:
                    showTimePickerDialog(view, START);
                    break;
                case R.id.etEndTime:
                    showTimePickerDialog(view, END);
                    break;
                default:
                    break;
            }
        }
    }

    public void showTimePickerDialog(View v, String type) {
        DialogFragment start = new StartTimePickerFragment();
        DialogFragment end = new EndTimePickerFragment();
        if (type.equals(START)) {
            start.show(getSupportFragmentManager(), "timePickerStartTime");
        } else {
            end.show(getSupportFragmentManager(), "timePickerEndTime");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etStartTime:
                showTimePickerDialog(view, START);
                break;
            case R.id.etEndTime:
                showTimePickerDialog(view, END);
                break;
            case R.id.btnAddEditEvent:
                addEditEventToDB();
                break;
            case R.id.btnDeleteEvent:
                deleteEventFromDB();
                break;
            case R.id.cbRemainder1:
                showRemainderSpinner(((CheckBox) view).isChecked(), REMAINDER_1);
                break;
            case R.id.cbRemainder2:
                showRemainderSpinner(((CheckBox) view).isChecked(), REMAINDER_2);
                break;
            case R.id.cbRemainder3:
                showRemainderSpinner(((CheckBox) view).isChecked(), REMAINDER_3);
                break;
            default:
                break;
        }
    }

    private void showRemainderSpinner(boolean checked, String remainderType) {
        if (checked) {
            switch (remainderType) {
                case REMAINDER_1:
                    spRemainder1.setVisibility(View.VISIBLE);
                    break;
                case REMAINDER_2:
                    spRemainder2.setVisibility(View.VISIBLE);
                    break;
                case REMAINDER_3:
                    spRemainder3.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        } else {
            switch (remainderType) {
                case REMAINDER_1:
                    spRemainder1.setVisibility(View.INVISIBLE);
                    break;
                case REMAINDER_2:
                    spRemainder2.setVisibility(View.INVISIBLE);
                    break;
                case REMAINDER_3:
                    spRemainder3.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    private void deleteEventFromDB() {
        eventEntity.delete(eventId);
        System.out.println("Event Deleted  ========= " + eventId);
        DynamicToast.makeSuccess(this, "Event deleted successfully").show();
        finish(); // Close the schedule activity
    }

    private void addEditEventToDB() {

        String eventName = Objects.requireNonNull(etEventName.getText()).toString();
        String eventLocation = Objects.requireNonNull(etEventLocation.getText()).toString();
        String eventDescription = Objects.requireNonNull(etEventDescription.getText()).toString();
        String eventStartTime = Objects.requireNonNull(etStartTime.getText()).toString();
        String eventEndTime = Objects.requireNonNull(etEndTime.getText()).toString();

        String eventRem1 = null;
        String eventRem2 = null;
        String eventRem3 = null;

        if (eventName.isEmpty()) {
            DynamicToast.makeError(this, "Please provide event name").show();
            return;
        }
        if (eventLocation.isEmpty()) {
            DynamicToast.makeError(this, "Please provide event location").show();;
            return;
        }
        if (eventStartTime.isEmpty()) {
            DynamicToast.makeError(this, "Please select event start time").show();;
            return;
        }
        String eventStartDateTime = eventDate + " " + eventStartTime;
        String eventEndDateTime = eventDate + " " + eventEndTime;

        if (!eventEndTime.isEmpty()) {
            if (!Helper.isEndTimeGreaterThanStartTime(eventStartDateTime, eventEndDateTime)) {
                DynamicToast.makeError(this, "Event end time should be greater than event start time").show();;
                return;
            }
        }


        if (cbRemainder1.isChecked()) {
            eventRem1 = spRemainder1.getSelectedItem().toString();
        }
        if (cbRemainder2.isChecked()) {
            eventRem2 = spRemainder2.getSelectedItem().toString();
        }
        if (cbRemainder3.isChecked()) {
            eventRem3 = spRemainder3.getSelectedItem().toString();
        }

        try {
            if (eventId > 0) {
                // Event exists; so update the values to the db
                Event event = new Event(eventId, eventName, eventLocation, eventDescription,
                        eventDate, eventStartTime, eventEndTime, eventRem1, eventRem2, eventRem3);
                eventEntity.update(event, eventId);
                System.out.println("Event Updated  ========= " + eventId);
                DynamicToast.makeSuccess(this, "Event updated successfully").show();
            } else {
                // New event; so create a new event
                long id = SharedPref.getKeyLastInsertedID(getApplicationContext()) + 1;
                Event event = new Event(id, eventName, eventLocation, eventDescription,
                        eventDate, eventStartTime, eventEndTime, eventRem1, eventRem2, eventRem3);
                eventEntity.create(event);
                SharedPref.setKeyLastInsertedID(getApplicationContext(), id);
                System.out.println("Event Created  ========= " + id);
                DynamicToast.makeSuccess(this, "Event created successfully").show();
            }
            finish(); // Close the schedule activity

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.spRemainder1:
                String spRem1 = adapterView.getItemAtPosition(position).toString();
                break;
            case R.id.spRemainder2:
                String spRem2 = adapterView.getItemAtPosition(position).toString();
                break;
            case R.id.spRemainder3:
                String spRem3 = adapterView.getItemAtPosition(position).toString();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public static class StartTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }


        @SuppressLint("DefaultLocale")
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
            TextInputEditText sTime = requireActivity().findViewById(R.id.etStartTime);
            sTime.setText(String.format("%02d:%02d", hour, minutes));
        }
    }


    public static class EndTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }


        @SuppressLint("DefaultLocale")
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
            TextInputEditText eTime = requireActivity().findViewById(R.id.etEndTime);
            eTime.setText(String.format("%02d:%02d", hour, minutes));
        }
    }
}