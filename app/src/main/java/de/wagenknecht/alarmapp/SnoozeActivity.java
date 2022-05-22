package de.wagenknecht.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.timepicker.MaterialTimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SnoozeActivity extends AppCompatActivity {

    Context context;
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
                returnToMain();
            }
        });
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmReceiver.stopPlayer();
                returnToMain();
            }
        });


    }

    private void setAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        //neue Alarmzeit
        long longAlarmTime = Calendar.getInstance().getTimeInMillis() + 60000 * usePreferences();

        DateFormat formatter = new SimpleDateFormat("HH:mm", Locale.GERMAN);
        formatter.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().toZoneId()));
        String stringAlarmTime = formatter.format(new Date(longAlarmTime));

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, longAlarmTime, pendingIntent);

        Intent mainIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE);

        Toast.makeText(this, "Alarm gesnoozet für " + usePreferences() + " min.", Toast.LENGTH_SHORT).show();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(SnoozeActivity.this, "weckerChannel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Wecker gesnoozt")
                .setContentText("Neuer Wecker für " + stringAlarmTime + " Uhr gestellt.")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent2);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(SnoozeActivity.this);
        notificationManagerCompat.notify(1, builder.build());
    }

    public int usePreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SnoozeActivity.this);
        String snoozeTimeString = prefs.getString("snoozeTime", "10");
        return Integer.parseInt(snoozeTimeString);
    }

    public void returnToMain() {
        Intent intent_main = new Intent(this, MainActivity.class);
        startActivity(intent_main);
    }
}