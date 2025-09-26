package com.polestar.carspeeddetails.network;
import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polestar.carspeeddetails.domain.model.User;

public class FirebaseService {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    User user = new User("0","0",0,"0");
    Context mContext;

    public FirebaseService(Context lContext) {
        // Get instance of Firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        // Get reference for the database
        databaseReference = firebaseDatabase.getReference("PersonData");
        mContext = lContext;
    }


    // method to saveData in Firebase
    public void addDataToFirebase(String id, String name, float currentSpeed) {
        user.setId(id);
        user.setName(name);
        user.setMaxSpeed(currentSpeed);
        user.setCommunicationChannel("Firebase");

        // Use push() to create a unique key for each person
        databaseReference.push().setValue(user)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(mContext, "Data added successfully!", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(error ->
                        Toast.makeText(mContext, "Failed to add data: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
