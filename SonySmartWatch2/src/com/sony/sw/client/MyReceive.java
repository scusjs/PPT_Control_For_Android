package com.sony.sw.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceive extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d(MyService.LOG_TAG, "onReceive: " + intent.getAction());
        intent.setClass(context, MyService.class);
        context.startService(intent);
    }
}
