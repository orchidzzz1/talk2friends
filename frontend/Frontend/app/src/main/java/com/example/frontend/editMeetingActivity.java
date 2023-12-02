package com.example.frontend;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import com.example.frontend.ClientAPI.ClientAPI;
import com.example.frontend.models.Meeting;
import org.json.JSONException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class editMeetingActivity extends AppCompatActivity {
    private Calendar selectedDateTime;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meeting);
        Intent intent = getIntent();
        ClientAPI api = new ClientAPI(this);
        Button editButton = findViewById(R.id.editMeetingButton);
        Button cancelBtn = findViewById(R.id.cancelEditMeetingButton);
        EditText editMeetingName = findViewById(R.id.editMeetingNameText);
        EditText editDescription = findViewById(R.id.editMeetingDescriptionText);
        TextView editDatetime = findViewById(R.id.editMeetingDateTimeText);
        EditText editLocation = findViewById(R.id.editMeetingTextLocation);
        // prefill with info
        editMeetingName.setText(intent.getStringExtra("meetingName"));
        editDescription.setText(intent.getStringExtra("meetingDescription"));
        editLocation.setText(intent.getStringExtra("meetingLocation"));
        editDatetime.setText(intent.getStringExtra("meetingDatetime"));

        Button btnPickDate = findViewById(R.id.editBtnPickDate);
        Button btnPickTime = findViewById(R.id.editBtnPickTime);

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
        String id = intent.getStringExtra("id");
        System.out.println(id);


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editMeetingName.getText().toString();
                String desc = editDescription.getText().toString();
                String dateTimeStr = editDatetime.getText().toString();
                String location = editLocation.getText().toString();
                if(title.equals("") || desc.equals("")|| location.equals("") || dateTimeStr.equals("")){
                    return;
                }

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

                Meeting meeting = new Meeting(id, title, timeInMillis, new String[]{},desc,location);
                try {
                    api.editMeeting(meeting);
                    openMeetingsPage();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMeetingsPage();
            }
        });
        Button removeBtn = findViewById(R.id.deleteMeetingButton);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    api.removeMeeting(intent.getStringExtra("id"));
                    openMeetingsPage();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

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
    private void updateSelectedDateTimeText() {
        TextView textViewSelectedDateTime = findViewById(R.id.editMeetingDateTimeText);
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

    public void openMeetingsPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
