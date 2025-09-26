package com.polestar.carspeeddetails.ui;

import android.Manifest;
import android.car.Car;
import android.car.hardware.property.CarPropertyManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.polestar.carspeeddetails.R;
import com.polestar.carspeeddetails.domain.usecases.CheckSpeed;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {
    private Car mCar;
    CarPropertyManager mCarPropertyManager;
    String[] perms = {"android.car.permission.CAR_SPEED", Manifest.permission.POST_NOTIFICATIONS};
    int permsRequestCode = 200;
    private static final String TAG = MainActivity.class.getSimpleName();
    CheckSpeed checkSpeed;

    private final String channelId = "com.polestar.carspeeddetails"; // Unique channel ID for notifications
    private final String description = "Speed Alert";  // Description for the notification channel
    NotificationManagerCompat lNotificationManager;
    NotificationCompat.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        requestPermissions(perms, permsRequestCode);
        // Create a notification channel
        createNotificationChannel();
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");

        super.onRequestPermissionsResult(permsRequestCode, permissions, grantResults);
        switch (permsRequestCode) {
            case 200:
                Log.d(TAG, "onRequestPermissionsResult: " + permsRequestCode);
                boolean carPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (carPermission) {

                    mCar = Car.createCar(this);
                    mCarPropertyManager = (CarPropertyManager) mCar.getCarManager(Car.PROPERTY_SERVICE);
                    checkSpeed = new CheckSpeed(MainActivity.this,mCar,mCarPropertyManager,lNotificationManager,builder);

                    Log.d(TAG, "onRequestPermissionsResult: isConnected " + mCar.isConnected());
                } else {
                    requestPermissions(perms, permsRequestCode);
                }
                break;
            default:
                Log.d(TAG, "onRequestPermissionsResult: default " + permsRequestCode);
                break;
        }
    }

    /**
     * Create a notification channel for devices running Android 8.0 or higher.
     * A channel groups notifications with similar behavior.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Custom layout for the notification content
            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.activity_notification);

            // Build the notification
             builder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_background) // Notification icon
                    .setContent(contentView) // Custom notification content
                    .setContentTitle("Warning!!!") // Title displayed in the notification
                    .setContentText("Car Reached Maximum Speed!") // Text displayed in the notification
                    .setAutoCancel(true) // Dismiss notification when tapped
                    .setPriority(NotificationCompat.PRIORITY_HIGH); // Notification priority for better visibility

            // Display the notification
            lNotificationManager = NotificationManagerCompat.from(this);
        }
    }
}