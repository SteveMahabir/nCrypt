package com.example.kevin.tempappp;


    import android.app.IntentService;
    import android.content.Intent;
    import android.os.IBinder;
    import android.util.Log;
    import android.widget.Toast;

public class nCryptService extends IntentService {
    private static final String TAG = "com.example.kevin.tempappp";
    public nCryptService() {
        super("");
    }
    /**
         * Creates an IntentService.  Invoked by your subclass's constructor.
         *
         * @param name Used to name the worker thread, important only for debugging.
         */
        public nCryptService(String name) {
            super(name);
        }
        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        protected void onHandleIntent(Intent intent) {

        }

        @Override
        public void onCreate() {
            // TODO Auto-generated method stub
            /*Toast.makeText(getApplicationContext(), "Service Created",Toast.LENGTH_SHORT).show();*/
            super.onCreate();
        }


        @Override
        public void onDestroy() {
            // TODO Auto-generated method stub
            /*Toast.makeText(getApplicationContext(), "Service Destroy",Toast.LENGTH_SHORT).show();*/
            super.onDestroy();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            // TODO Auto-generated method stub
            /*Toast.makeText(getApplicationContext(), "Service Working",Toast.LENGTH_SHORT).show();*/
            //return super.onStartCommand(intent, flags, startId);
            return START_STICKY;
        }
    }

