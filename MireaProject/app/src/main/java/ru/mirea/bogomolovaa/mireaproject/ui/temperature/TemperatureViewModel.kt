package ru.mirea.bogomolovaa.mireaproject.ui.temperature

import android.app.Application
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TemperatureViewModel : ViewModel() {
//    private val _temperature = MutableLiveData<Int>()
//    val temperature: LiveData<Int> = _temperature
//
//    init {
//        startTemperatureUpdates()
//    }
//
//    private fun startTemperatureUpdates() {
//        viewModelScope.launch {
//            while (true) {
//                val temperature = getTemperature()
//                _temperature.postValue(temperature)
//                delay(1000)
//            }
//        }
//    }
//
//    private suspend fun getTemperature(): Int {
//        val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
//        val temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
//            ?: throw IllegalStateException("Temperature sensor not found")
//
//        var temperature = 0f
//
//        val sensorEventListener = object : SensorEventListener {
//            override fun onSensorChanged(event: SensorEvent) {
//                if (event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
//                    if (event.sensor != null && event.values != null && event.values.isNotEmpty()) {
//                        temperature = event.values[0]
//                    }
//                }
//            }
//
//            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
//        }
//
//        sensorManager.registerListener(
//            sensorEventListener,
//            temperatureSensor,
//            SensorManager.SENSOR_DELAY_NORMAL
//        )
//
//        delay(1000)
//
//        sensorManager.unregisterListener(sensorEventListener)
//
//        return (temperature * 100).toInt()
//    }
}