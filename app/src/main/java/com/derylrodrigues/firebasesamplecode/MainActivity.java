package com.derylrodrigues.firebasesamplecode;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String FIREBASE_DB = "https://blazing-fire-1360.firebaseIO.com/";
    final Handler handler = new Handler();

    private EditText key;
    private EditText value;
    private Button save;

    private EditText fetchKey;
    private Button fetch;
    RemoteClient remoteClient;

    Timer timer;
    TimerTask timerTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        remoteClient = new RemoteClient(this);





        key = (EditText) findViewById(R.id.Key);
        value = (EditText) findViewById(R.id.Value);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remoteClient.saveValue(key.getText().toString(), value.getText().toString());
            }
        });

        fetchKey = (EditText) findViewById(R.id.FetchKey);
        fetch = (Button) findViewById(R.id.fetch);
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remoteClient.fetchValue(fetchKey.getText().toString());

                // any polling mechanism can be used
                startTimer(fetchKey.getText().toString());

            }
        });
    }

    public void startTimer(String key) {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask(key);
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms

        // The values can be adjusted depending on the performance
        timer.schedule(timerTask, 5000, 1000);
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask(final String key) {
        timerTask = new TimerTask() {
            public void run() {
                Log.d(TAG, "isDataFetched >>>>" + remoteClient.isDataFetched());
                if(remoteClient.isDataFetched())
                {
                    handler.post(new Runnable() {

                        public void run() {
                            Log.d(TAG, "Value >>>>" + remoteClient.getValue(key));
                           Toast.makeText(MainActivity.this, "Value   " + remoteClient.getValue(key), Toast.LENGTH_SHORT).show();
                        }
                    });

                    stoptimertask();
                }

            }
        };
    }



//    private void saveValue(String key, String value) {
//        Firebase ref = new Firebase(FIREBASE_DB);
//        Firebase usersRef = ref.child(key);
//        usersRef.setValue(value, new Firebase.CompletionListener() {
//            @Override
//            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
//                if (firebaseError != null) {
//                    Log.d(TAG, "Data could not be saved. " + firebaseError.getMessage());
//                } else {
//                    Log.d(TAG, "Data saved successfully.");
//                }
//            }
//        });
//    }

//    private void getValue(String key, final Context mContext) {
//
//        Log.d(TAG, "Get Value for Key - " + key);
//        Firebase ref = new Firebase(FIREBASE_DB + key);
//        Query queryRef = ref.orderByKey();
//        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                // snapshot contains the key and value
//                Toast.makeText(mContext, "Key - " + snapshot.getKey() + " - Value - " + snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
//
//                // do something with the key and value
//                // TODO:
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                Log.e(TAG, firebaseError.getMessage());
//                Log.e(TAG, firebaseError.getDetails());
//            }
//        });
//    }
}
