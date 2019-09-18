package com.example.kurilkachat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class testService extends Service {
    public testService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
