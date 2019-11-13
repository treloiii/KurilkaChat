package com.example.kurilkachat;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import static com.example.kurilkachat.App.CHANNEL_ID;

import io.crossbar.autobahn.websocket.WebSocketConnection;
import io.crossbar.autobahn.websocket.exceptions.WebSocketException;
import io.crossbar.autobahn.websocket.interfaces.IWebSocketConnectionHandler;
import io.crossbar.autobahn.websocket.types.ConnectionResponse;


public class BackgroundService extends Service {


    private final WebSocketConnection  socket = new WebSocketConnection();
    private IWebSocketConnectionHandler wsh;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction().equals("stop")){
            Log.d("forground","STOP");
            if (socket.isConnected()){
                socket.sendClose(1005,"stop service");
            }
            stopForeground(true);
            stopSelf();
        }
        else{
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Example Service")
                    .setContentText("Content")
                    .setSmallIcon(R.drawable.pachka)
                    .setContentIntent(pendingIntent)
                    .build();



            //do heavy work on a background thread
            //stopSelf();
            try{
                socket.connect("ws://188.225.27.155:8047/krkla",wsh=new IWebSocketConnectionHandler(){

                    @Override
                    public void onConnect(ConnectionResponse response) {
                        socket.sendMessage("{\"id\":\""+MainActivity.NICKNAME+"\",\"message\":\"connect\"}");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    socket.sendMessage("{\"id\":\""+MainActivity.NICKNAME+"\",\"message\":\"stillAlive\"}");
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void onOpen() {

                    }

                    @Override
                    public void onClose(int code, String reason) {
                        if(!reason.equals("stop service")) {
                            new android.os.Handler().postDelayed(
                                    () -> {
                                        try {
                                            socket.connect("ws://188.225.27.155:8047/krkla", wsh);
                                        } catch (WebSocketException e) {
                                            e.printStackTrace();
                                        }
                                    },
                                    5000);
                        }
                        else {
                            Log.d("onclose","Closed");
                        }
                    }

                    @Override
                    public void onMessage(String payload) {
                        try {
                            if (!payload.split("[:]")[1].equals(" 6a5df9fcac0cfa3b9b264f372dae311d") && !payload.split("[:]")[1].equals(" 7f021a1415b86f2d013b2618fb31ae53") && !payload.split("[:]")[1].equals(" b640a0ce465fa2a4150c36b305c1c11b") && !payload.split("[:]")[1].equals(" 9d634e1a156dc0c1611eb4c3cff57276") && !payload.split("[:]")[1].equals(" pong") && !payload.split("[:]")[1].equals("")&& !payload.split("[:]")[0].equals(MainActivity.NICKNAME)) {
                                NotificationHelper nh = new NotificationHelper(BackgroundService.this);
                                nh.createNotification(payload.split("[:]")[0], payload.split("[:]")[1]);
                            }
                        }
                        catch (Exception e){}
                    }

                    @Override
                    public void onMessage(byte[] payload, boolean isBinary) {

                    }

                    @Override
                    public void onPing() {

                    }

                    @Override
                    public void onPing(byte[] payload) {

                    }

                    @Override
                    public void onPong() {

                    }

                    @Override
                    public void onPong(byte[] payload) {

                    }

                    @Override
                    public void setConnection(WebSocketConnection connection) {

                    }
                });
            }
            catch (Exception e) {

            }
            startForeground(1, notification);
        }

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
