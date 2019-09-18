package com.example.kurilkachat;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import static com.example.kurilkachat.App.CHANNEL_ID;

import io.crossbar.autobahn.websocket.WebSocketConnection;

public class BackgroundService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText("Content")
                .setSmallIcon(R.drawable.pachka)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread
        //stopSelf();

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startForeground() {
        startForeground(0, new NotificationHelper(getApplicationContext()).createNotification("Background","Start In Background"));
    }


}
