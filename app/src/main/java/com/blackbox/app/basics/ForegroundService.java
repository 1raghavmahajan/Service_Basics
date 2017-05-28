package com.blackbox.app.basics;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.net.ConnectivityManager.TYPE_WIFI;


/*
A foreground service which registers a background receiver.
Won't be easily killed by android.

The service itself can be used to perform long running tasks or
the Broadcast receiver can be used to schedule that to perform on a certain event.
 (an alternate to the newer JobScheduler API)

*/



public class ForegroundService extends Service {

    //Tag for logging purposes
    public static final String TAG = ForegroundService.class.getSimpleName();

    //IntentFilter ACTION for BroadcastReceiver
    private static final String ACTION_IntentFilter = "android.net.conn.CONNECTIVITY_CHANGE";

    //Notification ID (can be used to kill it)
    private static final int NotificationID = 1459;


    //Broadcast Receiver object.
    private MyBroadcastReceiver br;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand  " + Thread.currentThread().getName());

        //Pending intent to be performed on click action on the notification.
        Intent notificationIntent = new Intent(this, OtherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new Notification.Builder(this)
                .setCategory(Notification.CATEGORY_SERVICE) //only in API 21 and above
                .setContentTitle("Important Service")
                .setContentText("Ongoing.")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MAX)
                .build();

        //If you want to just display a notification
        //NotificationManager NM = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        //NM.notify(NotificationID, notification);


        //Starting the foreground Service
        startForeground( NotificationID, notification);

        //Registering the BR
        IntentFilter intentFilter = new IntentFilter(ACTION_IntentFilter);
        br = new MyBroadcastReceiver();
        registerReceiver( br , intentFilter );

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy  " + Thread.currentThread().getName());
        unregisterReceiver(br); //Unregister to avoid memory leak
        stopForeground(true); // parameter true will lead to the killing of the notification as well
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Received " + Thread.currentThread().getName());


            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null)
            {
                if(networkInfo.getType() == TYPE_WIFI) { //if the intent was sent due to connection to Wifi
                    Log.i(TAG, "SSID: " + networkInfo.getExtraInfo());

                    //You can do anything here: startactivity / intentservice to perform a task etc.
                    Intent i = new Intent(context, OtherActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(i);
                }
            }
            else
            {
                Log.i(TAG, "NetworkInfo: null");
            }
        }
    }
}
