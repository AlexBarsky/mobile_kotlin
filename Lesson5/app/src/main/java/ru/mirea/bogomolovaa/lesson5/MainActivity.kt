package ru.mirea.bogomolovaa.lesson5

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.SimpleAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.bogomolovaa.lesson5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)

        //Для получения информации о сенсорах конкретного типа
        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val listSensor = binding.listView

        val arrayList = ArrayList<HashMap<String, Any>>()

        for (i in 0 until sensors.size) {
            val sensorTypeList = HashMap<String, Any>()
            sensorTypeList["Name"] = sensors[i].name
            sensorTypeList["Value"] = sensors[i].maximumRange
            arrayList.add(sensorTypeList)
        }

        val mHistory = SimpleAdapter(
            this,
            arrayList,
            android.R.layout.simple_list_item_2,
            arrayOf("Name","Value"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )

        listSensor.adapter = mHistory

        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val valueAzimuth = event.values[0]
            val valuePitch = event.values[1]
            val valueRoll = event.values[2]
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
}