package com.jtl.sensordemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.socks.library.KLog;

import java.util.List;

public class MainActivity extends Activity implements SensorEventListener {
    private TextView mSensorText;
    private TextView mCpuSensorText;
    private SensorManager mSensorManager;
    private Sensor mTemperatureSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorText = findViewById(R.id.tv_sensor_temperature);
        mCpuSensorText = findViewById(R.id.tv_sensor_temperature_cpu);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        KLog.init(true);
//
//        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
//        for (Sensor s : sensorList) {
//            KLog.w("Sensor", s.toString());
//        }

        boolean cpuTemperature = mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE), SensorManager.SENSOR_DELAY_FASTEST);
        boolean temperature = mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE), SensorManager.SENSOR_DELAY_FASTEST);

        KLog.e("Sensor", "cpuTemperature:" + cpuTemperature);
        KLog.e("Sensor", "temperature:" + temperature);
        mCpuSensorText.setText("是否支持Cpu温度Sensor："+ cpuTemperature);
        mSensorText.setText("是否支持普通温度Sensor：" + temperature);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        KLog.e("Sensor", Thread.currentThread().getName());
        if (event.sensor.getType() == Sensor.TYPE_TEMPERATURE) {
            final float temperatureValue = getTemperatureC(event.values[0]); // 得到温度
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCpuSensorText.setText("Cpu温度：" + temperatureValue + "℃");
                }
            });
        } else if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            final float temperatureValue1 = getTemperatureC(event.values[0]); // 得到温度
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSensorText.setText("周围温度：" + temperatureValue1 + "℃");
                }
            });
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private float getTemperatureC(float temperature) {
        return (float) (Math.round(temperature * 10)) / 10;
    }
}
