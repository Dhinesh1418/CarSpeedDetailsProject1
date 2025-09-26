package com.polestar.carspeeddetails.domain.usecases;

import android.annotation.SuppressLint;
import android.car.Car;
import android.car.VehiclePropertyIds;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.property.CarPropertyManager;
import android.content.Context;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.polestar.carspeeddetails.data.repo.UserData;
import com.polestar.carspeeddetails.network.AwsService;
import com.polestar.carspeeddetails.network.FirebaseService;

public class CheckSpeed {
    Car lCar;
    CarPropertyManager lCarPropertyManager;
    androidx.core.app.NotificationManagerCompat lNotificationManager;
    private static final String TAG = "CarSpeedDetails";

    public float currentSpeed;
    UserData userData;
    Context lContext;
    FirebaseService firebaseService;
    AwsService awsService;
    private final int notificationId = 1234; // Unique identifier for the notification
    private final String channelId = "com.polestar.carspeeddetails"; // Unique channel ID for notifications
    NotificationCompat.Builder lBuilder;


    public CheckSpeed(Context mContext, Car mCar, CarPropertyManager mCarPropertyManager, androidx.core.app.NotificationManagerCompat mNotificationManager,NotificationCompat.Builder mBuilder) {
        lContext = mContext;
        lCar = mCar;
        lCarPropertyManager = mCarPropertyManager;
        lNotificationManager = mNotificationManager;
        lBuilder = mBuilder;
        mCarPropertyManager.registerCallback(mCallBack, VehiclePropertyIds.PERF_VEHICLE_SPEED, CarPropertyManager.SENSOR_RATE_NORMAL);
        userData = new UserData();
        firebaseService = new FirebaseService(lContext);
        awsService = new AwsService();
        checkSpeed();
    }

    CarPropertyManager.CarPropertyEventCallback mCallBack = new CarPropertyManager.CarPropertyEventCallback() {
        @Override
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            Log.d(TAG, "onChangeEvent: " + carPropertyValue.toString());
            if (carPropertyValue.getPropertyId() == VehiclePropertyIds.PERF_VEHICLE_SPEED) {
                currentSpeed = (Float) carPropertyValue.getValue();
            }
        }
        @Override
        public void onErrorEvent(int i, int i1) {
            Log.e(TAG, "onErrorEvent: " + i);
        }
    };

    public void checkSpeed(){
        new Thread() {
            public void run() {
                while (true){

                    if (currentSpeed > (userData.getUser("001").getMaxSpeed())) {
                        firebaseService.addDataToFirebase(userData.getUser("001").getId(),userData.getUser("001").getName(),currentSpeed);
                        showWarningAlert();
                    } else if (currentSpeed > (userData.getUser("002").getMaxSpeed())) {
                        awsService.sendAlert(userData.getUser("002").getName(),currentSpeed);
                        showWarningAlert();
                    }
                }
            }
        }.start();
    }

    @SuppressLint("MissingPermission")
    private void showWarningAlert(){
        lNotificationManager.notify(notificationId, lBuilder.build());
    }
}
