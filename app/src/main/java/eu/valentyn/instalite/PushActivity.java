package eu.valentyn.instalite;

import androidx.appcompat.app.AppCompatActivity;


import android.app.NotificationManager;
import android.app.Notification.Builder;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import java.lang.Object;

import eu.valentyn.instalite.Model.Notification;

public class PushActivity extends AppCompatActivity {

    private NotificationManager nm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }
/*
    public void showNotification(View view){
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder
                .set
    }

 */
}