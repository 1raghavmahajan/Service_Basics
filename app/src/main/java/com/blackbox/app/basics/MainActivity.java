package com.blackbox.app.basics;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcel;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Handler handler;
    Button btn1;
    Button btn2;
    CheckBox checkBox;
    EditText editText;
    boolean isRunning;

    private static final String ACTION_FOO = "com.blackbox.app.basics.action.TIMER";
    private static final String ACTION_BAZ = "com.blackbox.app.basics.action.WAIT";
    private static final String ACTION_STOP = "com.blackbox.app.basics.action.STOP";
    private static final String EXTRA_PARAM1 = "com.blackbox.app.basics.extra.TIME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        editText = (EditText) findViewById(R.id.editText);
        handler = new Handler();
        isRunning = false;
    }


    public void hello(View view) {
       /* btn2.setText("dd");
        Intent i = new Intent(MainActivity.this, ServiceClass.class);
        i.putExtra("string", "potty");
        startService(i);
        Log.i("Random", "On Click");*/

        Intent i = new Intent(MainActivity.this, MyBRService.class);
        startService(i);
    }

    public void fn(View v) {
        /*btn1.setText("aa");
        Intent i = new Intent(MainActivity.this, ServiceClass.class);
        stopService(i);
        checkBox.setChecked(true);*/
        Intent i = new Intent(MainActivity.this, MyBRService.class);
        stopService(i);
    }


    public void startIntent(View view) {

        MyResultReceiver rr = new MyResultReceiver(handler);

        Intent i = new Intent(MainActivity.this, MyIntentService.class);
        i.setAction(ACTION_FOO);
        i.putExtra("receiver", rr);
        Integer time = Integer.valueOf(editText.getText().toString());
        if(time>0)
        {
            i.putExtra(EXTRA_PARAM1, time);
            startService(i);
        }
        else
            Toast.makeText(this, "Dumbass", Toast.LENGTH_SHORT).show();

        /*MyIntentService iService = new MyIntentService();
        iService.startActionFoo(this, Integer.valueOf(editText.getText().toString()));*/
    }

    public void startBR(View view) {
        if(isRunning==false) {
            Intent i = new Intent(MainActivity.this, MyIntentService.class);
            i.setAction(ACTION_BAZ);
            isRunning = true;
            startService(i);
        }
        else
        {
            Intent i = new Intent(MainActivity.this, MyIntentService.class);
            i.setAction(ACTION_STOP);
            isRunning = false;
            startService(i);
        }
    }

    public void startStickyService(View view) {
       /* Intent notificationIntent = new Intent(this, LoginActivity.class);
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
        NotificationManager NM = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        NM.notify(1459, notification);

        MyBRService myBR = new MyBRService();

        myBR.startForeground(1459, notification);
*/

        Intent notificationIntent = new Intent(this, MyBRService.class);
        startService(notificationIntent);

        /*notification.setLatestEventInfo(this, getText(R.string.notification_title),
                getText(R.string.notification_message), pendingIntent);*/

        //startForeground(ONGOING_NOTIFICATION_ID, notification);


    }

    public void otherFunction(View view) {
        Log.i("YOYO", "okay");
        startStickyService(view);
    }


    class MyResultReceiver extends ResultReceiver {
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            if(resultCode==69 && resultData != null)
            {
                final String s = resultData.get("final_counter").toString();
                Log.i("YOYO", "onReceiveResult: " + s);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("YOYO", "MyHandler" + Thread.currentThread().getName());
                        Toast.makeText(MainActivity.this, "potty: " + s, Toast.LENGTH_SHORT).show();
                        btn1.setText(s);
                    }
                });
            }
            else
                Log.i("YOYO", "onReceiveResult: error");


        }
    }
}
