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
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {

    private static final String ACTION_FOO = "com.blackbox.app.basics.action.TIMER";
    private static final String ACTION_BAZ = "com.blackbox.app.basics.action.WAIT";
    private static final String ACTION_STOP = "com.blackbox.app.basics.action.STOP";

    private static final String EXTRA_PARAM1 = "com.blackbox.app.basics.extra.TIME";

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("YOYO", "onHandleIntent  " + Thread.currentThread().getName());
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final Integer t = intent.getIntExtra(EXTRA_PARAM1, 0);
                handleActionFoo(t, intent);
            }
            else if(ACTION_BAZ.equals(intent.getAction()))
            {
                Log.i("YOYO", "ACTION_BAZ  " + Thread.currentThread().getName());
                IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
                MyBroadcastReceiver2 br = new MyBroadcastReceiver2();
                registerReceiver(br,intentFilter);
            }
            else if(ACTION_STOP.equals(intent.getAction()))
            {
                Log.i("YOYO", "ACTION_STOP  " + Thread.currentThread().getName());
            }
        }
    }

    private void handleActionFoo(Integer t, Intent intent) {
        Log.i("YOYO", "handleActionFoo  " + Thread.currentThread().getName());
        int i=0;
        while (i<t) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
            Log.i("YOYO", String.valueOf(i));
        }
        Bundle b = new Bundle();
        b.putInt("final_counter", i);
        ResultReceiver rr = intent.getParcelableExtra("receiver");
        rr.send(69, b);
    }

    class MyBroadcastReceiver2 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("YOYO", "receved " + Thread.currentThread().getName());
            Bundle extras = intent.getExtras();
            extras.get(ConnectivityManager.EXTRA_NETWORK_TYPE);

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);


            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            Log.i("YOYO", "recever " + networkInfo.toString());

        }
    }
}