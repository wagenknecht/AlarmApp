package de.wagenknecht.alarmapp;


import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private MaterialTimePicker picker;
    private Calendar calendar;
    private Calendar getTime;
    private static AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    //Buttons
    Button setAlarmButton;
    Button cancelAlarmButton;
    ImageButton settingsButton;

    TextView selectedTimeView;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        setAlarmButton = findViewById(R.id.setAlarmBtn);
        cancelAlarmButton = findViewById(R.id.cancelAlarmBtn);
        selectedTimeView = findViewById(R.id.selectedTimeView);
        settingsButton = findViewById(R.id.settingsButton);

        selectedTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        cancelAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_settings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent_settings);
            }
        });
    }

    private void cancelAlarm() {
        Intent intent = new Intent(this,AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm abgestellt", Toast.LENGTH_SHORT).show();
    }

    private void setAlarm() {
        if(calendar == null){
            Toast.makeText(this, "Bitte erst Alarm einstellen", Toast.LENGTH_SHORT).show();
        } else {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(this, AlarmReceiver.class);

            pendingIntent = PendingIntent.getBroadcast(this,0,intent, PendingIntent.FLAG_IMMUTABLE);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP , calendar.getTimeInMillis(), pendingIntent);

            Toast.makeText(this, "Alarm aktiviert", Toast.LENGTH_SHORT).show();
        }
    }

    private void showTimePicker() {
        getTime = Calendar.getInstance();

        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(getTime.get(Calendar.HOUR_OF_DAY))
                .setMinute(getTime.get(Calendar.MINUTE))
                .setTitleText("Uhrzeit stellen")
                .build();

        picker.show(getSupportFragmentManager(),"wecker");

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTimeView = findViewById(R.id.selectedTimeView);

                selectedTimeView.setText(String.format("%02d",picker.getHour())+" : "+String.format("%02d",picker.getMinute()));

                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,picker.getHour());
                calendar.set(Calendar.MINUTE,picker.getMinute());
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
            }
        });
    }

    private void createNotificationChannel() {
        CharSequence name = "Wecker Benachrichtigungs Kanal";
        NotificationChannel weckerChannel = new NotificationChannel("weckerChannel", name, NotificationManager.IMPORTANCE_HIGH);
        weckerChannel.setDescription("Benachrichtigungen vom Wecker außerhalb der App");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(weckerChannel);
    }
}