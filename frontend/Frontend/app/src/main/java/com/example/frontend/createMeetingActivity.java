package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.example.frontend.ClientAPI.ClientAPI;
import com.example.frontend.models.Meeting;

import org.json.JSONException;


public class createMeetingActivity extends AppCompatActivity {

    private Button createMeetingButton;
    ClientAPI api;
    private Calendar selectedDateTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);
        Button btnPickDate = findViewById(R.id.btnPickDate);
        Button btnPickTime = findViewById(R.id.btnPickTime);

        // Initialize the calendar instance for selected date and time
        selectedDateTime = Calendar.getInstance();

        // Set click listener for "Pick Date" button
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        // Set click listener for "Pick Time" button
        btnPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        RadioGroup radioGroupLocation =  findViewById(R.id.radioGroupLocation);
        RadioButton radioZoom = (RadioButton)findViewById(R.id.radioZoom);
        RadioButton radioPhysical = (RadioButton)findViewById(R.id.radioPhysical);
        EditText editTextPhysicalLocation = findViewById(R.id.editTextLocation);
        radioZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the button to generate Zoom link
                editTextPhysicalLocation.setVisibility(View.VISIBLE);
            }
        });

        // Set click listener for Physical radio button
        radioPhysical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the physical location field and hide the button to generate Zoom link
                editTextPhysicalLocation.setVisibility(View.VISIBLE);
            }
        });


        // initialize create meeting button
        createMeetingButton = (Button) findViewById(R.id.createNewMeetingButton);
        api = new ClientAPI(this);
        createMeetingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // get the details and pass to backend
                EditText titleEdit = findViewById(R.id.meetingNameText);
                String title = titleEdit.getText().toString();
                EditText descEdit = findViewById(R.id.meetingDescriptionText);
                String desc = descEdit.getText().toString();
                String location;
                TextView textViewSelectedDateTime = findViewById(R.id.meetingDateTimeText);
                String dateTimeStr = textViewSelectedDateTime.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                Date date = null;
                long timeInMillis;
                try {
                    date = sdf.parse(dateTimeStr);
                    // Get the time in milliseconds since the epoch
                    timeInMillis = date.getTime();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                EditText locationEdit = findViewById(R.id.editTextLocation);
                location = locationEdit.getText().toString();

                Meeting meeting = new Meeting("0", title, timeInMillis, new String[]{}, desc, location);
                try {
                    api.addMeeting(meeting);
                } catch (JSONException e) {
                    System.out.println("Failed to create meeting");
                }

                openMeetingsPage();
            }
        });
    }

    public void openMeetingsPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void updateSelectedDateTimeText() {
        TextView textViewSelectedDateTime = findViewById(R.id.meetingDateTimeText);
        // Update the TextView with the selected date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String formattedDateTime = dateFormat.format(selectedDateTime.getTime());
        textViewSelectedDateTime.setText(formattedDateTime);
    }
    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDateTime.set(Calendar.MINUTE, minute);

                        updateSelectedDateTimeText();
                    }
                },
                selectedDateTime.get(Calendar.HOUR_OF_DAY),
                selectedDateTime.get(Calendar.MINUTE),
                true); // 24-hour format

        timePickerDialog.show();
    }
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        selectedDateTime.set(Calendar.YEAR, year);
                        selectedDateTime.set(Calendar.MONTH, month);
                        selectedDateTime.set(Calendar.DAY_OF_MONTH, day);

                        updateSelectedDateTimeText();
                    }
                },
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}