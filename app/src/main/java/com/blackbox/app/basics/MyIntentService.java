package com.blackbox.app.basics;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

/**
 * An IntentService is used for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 * Kills itself after use.
 *
 * 2 tasks can be performed:
 * 1) Delay function using ACTION_TIMER
 * 2) Broadcast Receiver using ACTION_BR
 *
 * but Broadcast Receiver registered using an intent service can't serve well
 * as we won't be able to unregister the receiver
 * and it will lead to a leak.
 *
 * This will cause the receiver to receive the intent only once.
 *
 */


public class MyIntentService extends IntentService {

    static final String ACTION_TIMER = "com.blackbox.app.basics.action.TIMER";
    static final String ACTION_BR = "com.blackbox.app.basics.action.BR";
    static final String EXTRA_PARAM1 = "com.blackbox.app.basics.extra.TIME";

    private static final String TAG = MyIntentService.class.getSimpleName();

    public MyIntentService() {
        super("MyIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "onHandleIntent  " + Thread.currentThread().getName());

        if (intent != null) {

            final String action = intent.getAction();


            switch (action) {
                case ACTION_TIMER:
                    final Integer t = intent.getIntExtra(EXTRA_PARAM1, 0);
                    handleActionTimer(t, intent);
                    break;
                case ACTION_BR:
                    Log.i(TAG, "ACTION_BR  " + Thread.currentThread().getName());
                    IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
                    OtherBroadcastReceiver br = new OtherBroadcastReceiver();
                    registerReceiver(br, intentFilter);
                    break;
            }
        }
    }

    private void handleActionTimer(Integer t, Intent intent) {

        Log.i(TAG, "handleActionTimer:  " + Thread.currentThread().getName());

        //Simulates a long running operation.
        int i=0;
        while (i<t) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
            Log.i(TAG, "i= " + String.valueOf(i));
        }

        Bundle b = new Bundle();
        b.putInt("final_counter", i);
        //extract Result Receiver from Intent extra
        ResultReceiver rr = intent.getParcelableExtra("receiver");
        //Used to send the result.
        rr.send(69, b);
    }

    class OtherBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.i(TAG, "Received " + Thread.currentThread().getName());
        }
    }

}