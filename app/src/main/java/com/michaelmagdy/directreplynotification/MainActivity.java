package com.michaelmagdy.directreplynotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import static com.michaelmagdy.directreplynotification.Constants.CHANNEL_DESC;
import static com.michaelmagdy.directreplynotification.Constants.CHANNEL_NAME;
import static com.michaelmagdy.directreplynotification.Constants.CHANNNEL_ID;
import static com.michaelmagdy.directreplynotification.Constants.KEY_INTENT_HELP;
import static com.michaelmagdy.directreplynotification.Constants.KEY_INTENT_MORE;
import static com.michaelmagdy.directreplynotification.Constants.NOTIFICATION_ID;
import static com.michaelmagdy.directreplynotification.Constants.NOTIFICATION_REPLY;
import static com.michaelmagdy.directreplynotification.Constants.REQUEST_CODE_HELP;
import static com.michaelmagdy.directreplynotification.Constants.REQUEST_CODE_MORE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Constants constants = new Constants();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNNEL_ID, CHANNEL_NAME, importance);
            mChannel.setDescription(CHANNEL_DESC);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }

        Button button = findViewById(R.id.buttonCreateNotification);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayNotification();
            }
        });
    }

        public void displayNotification() {

            //Pending intent for a notification button named More
            PendingIntent morePendingIntent = PendingIntent.getBroadcast(
                    MainActivity.this,
                    REQUEST_CODE_MORE,
                    new Intent(MainActivity.this, NotificationReceiver.class)
                            .putExtra(KEY_INTENT_MORE, REQUEST_CODE_MORE),
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            //Pending intent for a notification button help
            PendingIntent helpPendingIntent = PendingIntent.getBroadcast(
                    MainActivity.this,
                    REQUEST_CODE_HELP,
                    new Intent(MainActivity.this, NotificationReceiver.class)
                            .putExtra(KEY_INTENT_HELP, REQUEST_CODE_HELP),
                    PendingIntent.FLAG_UPDATE_CURRENT
            );


            //We need this object for getting direct input from notification
            RemoteInput remoteInput = new RemoteInput.Builder(NOTIFICATION_REPLY)
                    .setLabel("Please enter your name")
                    .build();


            //For the remote input we need this action object
            NotificationCompat.Action action =
                    new NotificationCompat.Action.Builder(android.R.drawable.ic_delete,
                            "Reply Now...", helpPendingIntent)
                            .addRemoteInput(remoteInput)
                            .build();

            //Creating the notifiction builder object
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_email)
                    .setContentTitle("Hey this is Michael Magdy...")
                    .setContentText("Please share your name with us")
                    .setAutoCancel(true)
                    .setContentIntent(helpPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setLights(Color.RED, 3000, 3000)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                    .setStyle(new NotificationCompat.BigTextStyle())
                    .addAction(action)
                    .addAction(android.R.drawable.ic_menu_compass, "More", morePendingIntent)
                    .addAction(android.R.drawable.ic_menu_directions, "Help", helpPendingIntent);


            //finally displaying the notification
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
}
