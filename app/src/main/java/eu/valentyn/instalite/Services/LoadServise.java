package eu.valentyn.instalite.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class LoadServise extends Service {

    public void startTask() {
        TestTask task = new TestTask();

        // this is the same as calling task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        task.execute();

    }


    private void log(String msg) {
        Log.d("Load Service", msg);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static class TestTask extends AsyncTask /* Params, Progress, Result */ {
        public TestTask() {

        }

        @Override
        protected Void doInBackground(Object[] objects) {
            log("LoadService: entered");
            SystemClock.sleep(1000); // emulates some job


            log("LoadService: is about to finish");
            return null;
        }

        private void log(String msg) {
            Log.d("TestTask #" + 1, msg);
        }


    }
}
