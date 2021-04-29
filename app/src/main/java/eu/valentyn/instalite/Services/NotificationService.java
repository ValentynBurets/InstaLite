package eu.valentyn.instalite.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import eu.valentyn.instalite.Model.Post;
import eu.valentyn.instalite.Model.User;
import eu.valentyn.instalite.R;

public class NotificationService extends Service {
    private NotificationManager mNM;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;
    eu.valentyn.instalite.Model.Notification notification = null;
    Context mContext;

    public void startService(eu.valentyn.instalite.Model.Notification notification, Context mContext) {
        this.notification = notification;
        this.mContext = mContext;
    }


    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        NotificationService getService() {
            return NotificationService.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = findUser() + notification.getText() + getPostInfo() + "that you posted earlier!!! :)";

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(getText(R.string.local_service_label))  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .build();

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }

    private String findUser(){
        final String[] username = new String[1];

        FirebaseDatabase.getInstance().getReference().child("Users").child(notification.getUserid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username[0] = user.getName() + " " + user.getUsername();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        return username[0];
    }

    private String getPostInfo(){
        final String[] postInfo = new String[1];

        FirebaseDatabase.getInstance().getReference().child("Posts").child(notification.getPostid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                postInfo[0] = post.getDescription();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        return postInfo[0];
    }
}