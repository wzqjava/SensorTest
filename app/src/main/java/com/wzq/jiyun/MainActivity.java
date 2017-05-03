package com.wzq.jiyun;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private Vibrator vibrator;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Toast.makeText(MainActivity.this, "手机摇一摇了", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensorManager.registerListener(
                    sensorEventListener,
                    //Accelerometer - 加速规; 加速度计; 加速计
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    //频率,使用手机默认的
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    /**
     * 取消注册
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }
    SensorEventListener sensorEventListener =  new SensorEventListener(){

       @Override
       public void onSensorChanged(SensorEvent event) {
           float[] values = event.values;
           Toast.makeText(MainActivity.this, "进入回调了", Toast.LENGTH_SHORT).show();
           Log.e(TAG, "onSensorChanged: ");
           float x = values[0];
           float y = values[1];
           float z = values[2];
           Log.e(TAG, x + "-----" + y+"-----" + z);
           // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
           int tempValue = 10;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
           if ((Math.abs(x) > tempValue) ||(Math.abs(y) > tempValue) || (Math.abs(z) > tempValue)){
                vibrator.vibrate(200);
               Message message = new Message();
               message.what = 1;
               handler.sendMessage(message);

           }

       }

       @Override
       public void onAccuracyChanged(Sensor sensor, int accuracy) {

       }
   };

    /**
     * 点击文本,显示手机支持的所有传感器,和名字等信息;
     * @param view
     */
    public void testSensor(View view) {

        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i = 0; i < sensorList.size(); i++) {
            Sensor sensor = sensorList.get(i);
            String name = sensor.getName();
            int type = sensor.getType();
            String vendor = sensor.getVendor();

            Log.e(TAG, "name:"+name +"---tyep:"+type+"---vendor:"+vendor);
        }

    }
}
