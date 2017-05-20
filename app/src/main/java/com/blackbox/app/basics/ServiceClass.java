package com.blackbox.app.basics;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Raghav on 15-May-17.
 */

public class ServiceClass extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("yoyo","onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //@IntDef(value = {Service.START_FLAG_REDELIVERY, Service.START_FLAG_RETRY}, flag = true)
        Log.i("yoyo","onStartCommand " + intent.getStringExtra("string"));

        new AsyncClass().execute(20);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("yoyo","onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("yoyo","onBind");
        return null;
    }


    class AsyncClass extends AsyncTask<Integer, Integer, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Toast.makeText(ServiceClass.this, "now " + values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Integer... params) {

            int x  = params[0], c = 1;
            while (c <= x) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(c);
                c++;
            }
            return null;
        }
    }
}
