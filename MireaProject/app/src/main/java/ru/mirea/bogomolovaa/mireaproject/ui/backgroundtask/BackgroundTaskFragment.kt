package ru.mirea.bogomolovaa.mireaproject.ui.backgroundtask

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import ru.mirea.bogomolovaa.mireaproject.R
import ru.mirea.bogomolovaa.mireaproject.databinding.FragmentBackgroundTaskBinding

class BackgroundTaskFragment : Fragment() {

    companion object {
        fun newInstance() = BackgroundTaskFragment()
        const val NUMBER_OF_POINTS_KEY = "numberOfPoints"
        const val PI_APPROXIMATION_KEY = "piApproximation"
    }

    private val viewModel: BackgroundTaskViewModel by activityViewModels()
    private var _binding: FragmentBackgroundTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBackgroundTaskBinding.inflate(inflater, container, false)
        val root = binding.root

        val fragmentDescTextView = binding.fragmentDescTextView
        val inputFieldView = binding.inputFieldView
        val resultTextView = binding.resultTextView

        viewModel.textViewDescription.observe(viewLifecycleOwner) {
            fragmentDescTextView.text = getString(it)
        }
        viewModel.resultTextView.observeForever {
            if (it.isNotEmpty()) {
                resultTextView.text = it
            }
        }

        val workManager = WorkManager
            .getInstance(requireContext())

        binding.btnStartWorker.setOnClickListener {
            val inputText = inputFieldView.text.toString()

            if (inputText.isEmpty()) {
                Snackbar.make(
                    inputFieldView,
                    R.string.empty_input_field,
                    Snackbar.LENGTH_LONG
                ).show()
            } else if (inputText.toLongOrNull() == null) {
                Snackbar.make(
                    inputFieldView,
                    R.string.invalid_input_field,
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                val inputData = Data
                    .Builder()
                    .putLong(NUMBER_OF_POINTS_KEY, inputText.toLong())
                    .build()

                val factorialCalcWorkRequest = OneTimeWorkRequest
                    .Builder(PiCalculationWorker::class.java)
                    .setInputData(inputData)
                    .setConstraints(
                        Constraints
                            .Builder()
                            .setRequiredNetworkType(NetworkType.UNMETERED)
                            .build()
                    )
                    .build()

                workManager.enqueue(factorialCalcWorkRequest)

                Snackbar.make(
                    inputFieldView,
                    getString(R.string.worker_started, inputText.toLong()),
                    Snackbar.LENGTH_LONG
                ).show()

                workManager
                    .getWorkInfoByIdLiveData(factorialCalcWorkRequest.id)
                    .observeForever { workInfo ->
                        if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                            val result = workInfo.outputData.getDouble(PI_APPROXIMATION_KEY, 3.14)
                            viewModel.resultTextView.apply { value = "Result: $result" }
                        }
                    }
            }
        }
        return root
    }
}