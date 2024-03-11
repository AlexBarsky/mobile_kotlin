package ru.mirea.bogomolovaa.mireaproject.ui.temperature

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import ru.mirea.bogomolovaa.mireaproject.R
import ru.mirea.bogomolovaa.mireaproject.databinding.FragmentTemperatureBinding
import kotlin.math.max
import kotlin.math.min

class TemperatureFragment : Fragment(), SensorEventListener {

    companion object {
        fun newInstance() = TemperatureFragment()
        val TAG: String = TemperatureFragment::class.java.simpleName

        private const val MIN_TEMPERATURE = -273.1f
        private const val MAX_TEMPERATURE = 100.0f

        private const val MIN_HEIGHT = 0
        private const val MAX_HEIGHT = 1050
    }

    private val viewModel: TemperatureViewModel by viewModels()

    private var _binding: FragmentTemperatureBinding? = null
    private val binding get() = _binding!!

    private var temperatureSensor: Sensor? = null

    private lateinit var sensorManager: SensorManager
    private lateinit var temperatureTextView: TextView
    private lateinit var thermometerFillHotView: View
    private lateinit var thermometerFillColdView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTemperatureBinding.inflate(inflater, container, false)

        temperatureTextView = binding.temperatureTextView
        thermometerFillHotView = binding.thermometerFillHot
        thermometerFillColdView = binding.thermometerFillCold

        sensorManager = requireContext().getSystemService(SENSOR_SERVICE) as SensorManager
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        if (temperatureSensor == null) {
            Toast.makeText(
                requireContext(),
                "Temperature sensor not available",
                Toast.LENGTH_LONG
            ).show()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        temperatureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }

    private fun calculateFillHeight(temperature: Float): Int {
        val percentage = (temperature - MIN_TEMPERATURE) / (MAX_TEMPERATURE - MIN_TEMPERATURE)
        val fillHeight = (percentage * MAX_HEIGHT).toInt()

        return min(MAX_HEIGHT, max(MIN_HEIGHT, fillHeight))
    }

    private fun calculateFillColors(temperature: Float) {
        val percentage = (temperature - MIN_TEMPERATURE) / (MAX_TEMPERATURE - MIN_TEMPERATURE)
        val height = (percentage * MAX_HEIGHT).toInt()
        val fillHeight = min(MAX_HEIGHT, max(MIN_HEIGHT, height))

        thermometerFillHotView.layoutParams.height = fillHeight
        thermometerFillColdView.layoutParams.height = MAX_HEIGHT - fillHeight
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                val temperature = it.values[0]
                temperatureTextView.text = getString(R.string.temperature_format, temperature.toInt())

                calculateFillColors(temperature)
                thermometerFillHotView.requestLayout()
                thermometerFillColdView.requestLayout()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}