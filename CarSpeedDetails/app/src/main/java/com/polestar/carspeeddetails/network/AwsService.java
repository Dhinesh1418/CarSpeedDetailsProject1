package com.polestar.carspeeddetails.network;

public class AwsService {

    //method to sendAlert - AWS
    public void sendAlert(String userName,float currentSpeed){
        System.out.println("AMS Message user - "+userName+" exceeded the speed limit and the currentSpeed is "+currentSpeed);
    }
}
