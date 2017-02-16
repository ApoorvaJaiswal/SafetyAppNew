package com.example.user.safetyappnew;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class shake_check extends Service implements SensorEventListener {
        private SensorManager sm;
        private Sensor acc;
        static int count = 0;

        public shake_check() {
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            //throw new UnsupportedOperationException("Not yet implemented");
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            // Intent en= getIntent();
            //Toast.makeText(this, "In onCreate shake_check", Toast.LENGTH_SHORT).show();
            //startService(i);
            sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            acc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       // Toast.makeText(this,"In onstart command",Toast.LENGTH_SHORT).show();
        sm.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
        NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent in= new Intent(this,ToggleButton.class);
        PendingIntent pi= PendingIntent.getActivity(this,0,in,0);
        NotificationCompat.Builder b= new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("title").setContentText("abc").setAutoCancel(true).setOngoing(true).setContentIntent(pi);
        barNotif= b.build();
        startForeground(1,barNotif);

        return START_STICKY;   //super.onStartCommand(intent, flags, startId);
    }

    /*@Override
              protected void onResume() {
                  super.onResume();
                  sm.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
              }

              @Override
              protected void onPause() {
                  super.onPause();
                  sm.unregisterListener(this);
              }
          */
        @Override
        public void onSensorChanged(SensorEvent event) {

            //Toast.makeText(this,"sensor changed",Toast.LENGTH_SHORT).show();

            float x = 0.0f, y = 0.0f, z = 0.0f, x1 = 0.0f, y1 = 0.0f, z1 = 0.0f, lax, lay, laz, lax1, lay1, laz1, max1 = 0.0f, max = 0.0f;
        //low pass filter using alpha=0.8 0.2=1-0.8
        x1 = (float) (0.8 * x + 0.2 * event.values[0]);
        y1 = (float) (0.8 * y + 0.2 * event.values[1]);
        z1 = (float) (0.8 * z + 0.2 * event.values[2]);
        //high pass filter
        lax1 = x1 - event.values[0];
        lay1 = y1 - event.values[1];
        laz1 = z1 - event.values[2];
        lax = x - event.values[0];
        lay = y - event.values[1];
        laz = z - event.values[2];
        float lx1 = Math.abs(lax1);
        float ly1 = Math.abs(lay1);
        float lz1 = Math.abs(laz1);
        float lz = Math.abs(laz);
        float ly = Math.abs(lay);
        float lx = Math.abs(lax);
        max1 = (lx1 > ly1) ? ((lx1 > lz1) ? lx1 : lz1) : (ly1 > lz1 ? ly1 : lz1);
        max = (lx > ly) ? ((lx > lz) ? lx : lz) : (ly > lz ? ly : lz);

        if (Math.abs((max1 - max)) >= 4.2) {
            ++count;
        }
        if (count == 3) {
            count = 0;
            Toast.makeText(this, "Shook 3 times", Toast.LENGTH_SHORT).show();

        }
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
