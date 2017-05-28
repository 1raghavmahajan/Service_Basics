package com.blackbox.app.basics;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Difference between a Service class an Intent Service is that
 * ServiceClass runs on the main UI thread, thus we can't perform long running operations as it is
 * We use ASync Class to do that.
 *
 */

public class ServiceClass extends Service {

    private static final String TAG = ServiceClass.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG,"onStartCommand");

        int timerDuration = intent.getIntExtra("timerValue", 0);
        if(timerDuration>0)
            new AsyncClass().execute(timerDuration);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind");
        return null;
    }


    private class AsyncClass extends AsyncTask<Integer, Integer, Void>
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
            Toast.makeText(ServiceClass.this, "Now " + values[0], Toast.LENGTH_SHORT).show();
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
