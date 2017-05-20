package com.blackbox.app.basics;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyBRService extends Service {
    private MyBroadcastReceiver br;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // @IntDef(value = {Service.START_FLAG_REDELIVERY, Service.START_FLAG_RETRY}, flag = true)
        Log.i("YOYO", "onStartCommand  " + Thread.currentThread().getName());
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        br = new MyBroadcastReceiver();

        Intent notificationIntent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new Notification.Builder(this)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentTitle("Important Service")
                .setContentText("Please don't kill me")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MAX)
                .build();
        //NotificationManager NM = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        //NM.notify(1459, notification);

        startForeground(1459, notification);

        registerReceiver(br,intentFilter);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("YOYO", "onDestroy  " + Thread.currentThread().getName());
        unregisterReceiver(br);
        stopForeground(true);
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
            Log.i("YOYO", "received " + Thread.currentThread().getName());


            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null)
            {
                NetworkInfo.State state = networkInfo.getState();
                Log.i("YOYO", "NetworkInfo: " + networkInfo.toString());
                Intent i = new Intent(context, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i);
            }
            else
            {
                Log.i("YOYO", "NetworkInfo: null");
            }
        }
    }
}
