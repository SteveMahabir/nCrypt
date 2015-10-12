package com.example.kevin.tempappp;


    import android.app.IntentService;
    import android.content.Intent;
    import android.util.Log;

public class nCryptService extends IntentService {
    private static final String TAG = "com.example.kevin.tempappp";
        /**
         * Creates an IntentService.  Invoked by your subclass's constructor.
         *
         * @param name Used to name the worker thread, important only for debugging.
         */
        public nCryptService(String name) {
            super(name);
        }

        @Override
        protected void onHandleIntent(Intent arg0) {
            // TODO Auto-generated method stub
            Log.i(TAG, "Intent Service started");

        }

    }

