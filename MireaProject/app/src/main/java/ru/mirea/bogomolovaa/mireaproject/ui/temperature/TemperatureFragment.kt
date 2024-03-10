package ru.mirea.bogomolovaa.mireaproject.ui.temperature

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mirea.bogomolovaa.mireaproject.R

class TemperatureFragment : Fragment() {

    companion object {
        fun newInstance() = TemperatureFragment()
    }

    private val viewModel: TemperatureViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_temperature, container, false)
    }
}