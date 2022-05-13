package de.wagenknecht.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;

import java.util.Calendar;

public class SnoozeActivity extends AppCompatActivity {

    private Context context;
    Button buttonSnooze;
    Button buttonStop;

    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public SnoozeActivity() {
    }

    public SnoozeActivity(Context context) {
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooze);

        buttonSnooze = findViewById(R.id.buttonSnooze);
        buttonStop = findViewById(R.id.buttonStop);
        buttonSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmReceiver.stopPlayer();
                setAlarm();
            }
        });
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmReceiver.stopPlayer();
            }
        });


    }

    private void setAlarm() {

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this,0,intent, PendingIntent.FLAG_MUTABLE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP , calendar.getTimeInMillis() + 60000 * usePreferences(), pendingIntent);

        Toast.makeText(this, "Alarm aktiviert", Toast.LENGTH_SHORT).show();
    }

    public int usePreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getInt("snoozeTime", 10);
    }
}