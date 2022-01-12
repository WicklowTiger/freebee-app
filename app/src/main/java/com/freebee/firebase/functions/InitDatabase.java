package com.freebee.firebase.functions;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class InitDatabase extends Thread {
    @Override
    public void run() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Constanta");
        city.put("restaurants", new HashMap<>());
        firestore.collection("countries/Romania/cities")
                .add(city)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("InitDatabase", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("InitDatabase", "Error adding document", e);
                    }
                });
    }
}
