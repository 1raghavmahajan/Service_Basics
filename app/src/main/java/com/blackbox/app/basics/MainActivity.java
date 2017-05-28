package com.blackbox.app.basics;

import android.content.Intent;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 *
 *
 */


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Handler handler;
    Button btn1;
    Button btn2;
    CheckBox checkBox;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        editText = (EditText) findViewById(R.id.editText);
        handler = new Handler();

    }


    public void startForegroundService(View view) {
       /* btn2.setText("dd");
        Intent i = new Intent(MainActivity.this, ServiceClass.class);
        i.putExtra("string", "potty");
        startService(i);
        Log.i("Random", "On Click");*/

        Intent i = new Intent(MainActivity.this, ForegroundService.class);
        startService(i);
    }

    public void stopForegroundService(View v) {
        /*btn1.setText("aa");
        Intent i = new Intent(MainActivity.this, ServiceClass.class);
        stopService(i);
        checkBox.setChecked(true);*/
        Intent i = new Intent(MainActivity.this, ForegroundService.class);
        stopService(i);
    }


    /**
     * Intent service which mimics a long operation using set delay.
     * Amount of seconds are controlled by the integer in the Edit Text
     */
    public void startIntent(View view) {

        MyResultReceiver myResultReceiver = new MyResultReceiver(handler);

        Intent i = new Intent(MainActivity.this, MyIntentService.class);

        //Can be used to control which action to perform
        i.setAction(MyIntentService.ACTION_TIMER);


        i.putExtra("receiver", myResultReceiver);
        Integer time = Integer.valueOf(editText.getText().toString());
        if(time>0)
        {
            i.putExtra(MyIntentService.EXTRA_PARAM1, time);
            startService(i);
        }
        else
            Toast.makeText(this, "Put something more than 0 my dear.", Toast.LENGTH_SHORT).show();


    }

    public void startBR(View view) {

        Intent i = new Intent(MainActivity.this, MyIntentService.class);
        i.setAction(MyIntentService.ACTION_BR);
        startService(i);

    }

    //starts the normal service
    public void startServiceClass(View view) {
        Intent i = new Intent(this, ServiceClass.class);
        i.putExtra("timerValue",Integer.valueOf(editText.getText().toString()));
        startService(i);
    }


    class MyResultReceiver extends ResultReceiver {

        /**
         *  An intent service has no means of communication with the callee and stops itself after the task is done.
         *  A result receiver can be used to get the result after the task is done.
         *  Gets a bundle object after receiving.
         *  A handler is passed so that methods can be performed related to UI change as the onReceiveResult is in the worker thread.
         */

        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            super.onReceiveResult(resultCode, resultData);

            //ResultCode is used to verify the sender.
            if(resultCode==69 && resultData != null)
            {

                final String s = String.valueOf(resultData.getInt("final_counter"));

                Log.i(TAG, "onReceiveResult: " + s);

                //To perform functions on the UI thread.
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.i(TAG, "MyHandler: " + Thread.currentThread().getName());

                        //btn1.setText()

                        Toast.makeText(MainActivity.this, "Final counter:" + s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
                Log.i(TAG, "onReceiveResult: error");

        }
    }
}
