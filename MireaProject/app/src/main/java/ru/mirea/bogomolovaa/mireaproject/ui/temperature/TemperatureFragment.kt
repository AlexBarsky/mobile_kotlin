package ru.mirea.bogomolovaa.mireaproject.ui.temperature

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import ru.mirea.bogomolovaa.mireaproject.R
import ru.mirea.bogomolovaa.mireaproject.databinding.FragmentTemperatureBinding

class TemperatureFragment : Fragment(), SensorEventListener {

    companion object {
        fun newInstance() = TemperatureFragment()
        val TAG: String = TemperatureFragment::class.java.simpleName
    }

    private val viewModel: TemperatureViewModel by viewModels()

    private var _binding: FragmentTemperatureBinding? = null
    private val binding get() = _binding!!

    private lateinit var sensorManager: SensorManager
    private var temperatureSensor: Sensor? = null
    private lateinit var thermometerImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTemperatureBinding.inflate(inflater, container, false)

        thermometerImageView = binding.thermometerImageView

        sensorManager = requireContext().getSystemService(SENSOR_SERVICE) as SensorManager
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        if (temperatureSensor == null) {
            Toast.makeText(
                requireContext(),
                "Temperature sensor not available",
                Toast.LENGTH_SHORT
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

    private fun updateUI(temperature: Float) {
        val drawableId = when {
            temperature < 10 -> R.drawable.ic_thermometer_cold
            temperature in 10.0..30.0 -> R.drawable.ic_thermometer_medium
            else -> R.drawable.ic_thermometer_hot
        }
        thermometerImageView.setImageResource(drawableId)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                val temperature = it.values[0]
                updateUI(temperature)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}