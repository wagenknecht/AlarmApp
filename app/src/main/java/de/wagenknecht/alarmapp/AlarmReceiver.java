package de.wagenknecht.alarmapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private static MediaPlayer player;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, SnoozeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setClassName("de.wagenknecht.alarmapp", "de.wagenknecht.alarmapp.SnoozeActivity");
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"weckerChannel")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Wecker klingelt")
                .setContentText("Dein gestellter Wecker klingelt")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());


        context.startActivity(i);

        player = MediaPlayer.create(context, R.raw.alarmsound);
        player.setLooping(true);
        player.start();
    }

    public static void stopPlayer(){
        player.stop();
    }
}