package ru.mirea.bogomolovaa.mireaproject.ui.data

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.mirea.bogomolovaa.mireaproject.R
import ru.mirea.bogomolovaa.mireaproject.databinding.FragmentDataBinding

class DataFragment : Fragment() {

    private var _binding: FragmentDataBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataViewModel =
            ViewModelProvider(this)[DataViewModel::class.java]

        _binding = FragmentDataBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textViewTitle: TextView = binding.textViewTitle
        val textViewDescription: TextView = binding.textViewDescription
        dataViewModel.textViewTitle.observe(viewLifecycleOwner) {
            textViewTitle.text = it
        }
        dataViewModel.textViewDescription.observe(viewLifecycleOwner) {
            textViewDescription.text = it
        }
        return root
    }
}