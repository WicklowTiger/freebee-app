package com.freebee.firebase;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.freebee.firebase.functions.InitDatabase;
import com.freebee.shared.Callback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


public class FirebaseManager {
    private static final Map<String, Class<? extends Thread>> functions = new HashMap<String, Class<? extends Thread>>() {{
        put("InitDatabase", InitDatabase.class);
    }};

    public static void runFunction(String className) {
        Class<? extends Thread> fnClassDef = functions.get(className);
        if (fnClassDef == null) {
            Log.e("fnError", "Function " + className + " does not exist! Add it to FirebaseManager.functions");
            return;
        }
        try {
            fnClassDef.newInstance().start();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static void getCollection(String path, Callback<QuerySnapshot> fn) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(path)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            fn.call(task.getResult());
                        } else {
                            Log.d("FirebaseManager", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static void getDocumentByName(String collectionPath, String name, Callback<DocumentSnapshot> fn) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(collectionPath)
                .whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            fn.call(task.getResult().getDocuments().get(0));
                        } else {
                            Log.d("FirebaseManager", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
