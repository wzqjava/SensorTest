package com.wzq.jiyun.sensordemo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private Sensor defaultSensor;
    private SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //活去系统服务里面的一个magager;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //得到了一个加速度传感器
        defaultSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] values = event.values;
                float x = values[0];
                float y = values[1];
                float z = values[2];
                Log.e(TAG, x + "-----" + y+"-----" + z);
                // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
                int tempValue = 9;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
                if(Math.abs(x)>tempValue  || Math.abs(y)>tempValue || Math.abs(z)>tempValue){
                    Toast.makeText(MainActivity.this, "我在摇一摇", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(sensorEventListener,defaultSensor,sensorManager.SENSOR_DELAY_NORMAL);

    }


    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }



    //获取手机支持哪些传感器
    public void testSensor(View view) {
        //Accelerometer,加速度
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (int i = 0; i <sensorList.size() ; i++) {
            Sensor sensor = sensorList.get(i);
            String name = sensor.getName();
            String vendor = sensor.getVendor();
            Log.e(TAG, "name:"+name+"----vendor"+vendor );
        }


    }
}
