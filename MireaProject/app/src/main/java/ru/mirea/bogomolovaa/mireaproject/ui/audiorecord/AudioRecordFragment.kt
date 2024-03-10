package ru.mirea.bogomolovaa.mireaproject.ui.audiorecord

import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import ru.mirea.bogomolovaa.mireaproject.R
import ru.mirea.bogomolovaa.mireaproject.databinding.FragmentAudioRecordBinding
import java.io.File
import java.io.IOException

class AudioRecordFragment : Fragment() {

    companion object {
        fun newInstance() = AudioRecordFragment()
        val TAG: String = AudioRecordFragment::class.java.simpleName
    }

    private val viewModel: AudioRecordViewModel by activityViewModels()
    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>

    private var _binding: FragmentAudioRecordBinding? = null
    private val binding get() = _binding!!

    private var _recorder: MediaRecorder? = null
    private val recorder: MediaRecorder get() = _recorder!!

    private var _player: MediaPlayer? = null
    private val player: MediaPlayer get() = _player!!

    private lateinit var recordFilePath: String
    private var isStartRecording = true
    private var isStartPlaying = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioRecordBinding.inflate(inflater, container, false)

        val recordButton = binding.recordButton
        val playButton = binding.playButton
        playButton.isEnabled = false

        recordFilePath = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            "/audiorecordtest.3gp"
        ).absolutePath

        requestPermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                if (ContextCompat.checkSelfPermission(requireContext(), it.key) != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(it.key)) {
                        Snackbar.make(
                            binding.root,
                            "Our app needs access to your device's microphone to record audio. " +
                                    "Please grant this permission in your device settings.",
                            Snackbar.LENGTH_LONG
                        ).setAction("Go to settings") {
                            try {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                intent.data = Uri.fromParts(
                                    "package",
                                    requireContext().packageName,
                                    null
                                )
                                startActivity(intent)
                            } catch (e: ActivityNotFoundException) {
                                Snackbar.make(
                                    binding.root,
                                    "Unable to open settings. " +
                                            "Please open your device settings and grant the permission manually.",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }.show()
                    }
                }
            }
        }

        recordButton.setOnClickListener {
            when (isStartRecording) {
                true -> {
                    recordButton.text = getString(R.string.stop_recording)
                    playButton.isEnabled = false
                    startRecording()
                }
                false -> {
                    recordButton.text = getString(R.string.start_recording)
                    playButton.isEnabled = true
                    stopRecording()
                }
            }
            isStartRecording =! isStartRecording
        }

        playButton.setOnClickListener {
            when (isStartPlaying) {
                true -> {
                    playButton.text = getString(R.string.stop_playing)
                    recordButton.isEnabled = false
                    startPlaying()
                }
                false -> {
                    playButton.text = getString(R.string.start_playing)
                    recordButton.isEnabled = true
                    stopPlaying()
                }
            }
            isStartPlaying =! isStartPlaying
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        requestPermissionsLauncher.launch(arrayOf(RECORD_AUDIO, WRITE_EXTERNAL_STORAGE))
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun startRecording() {
        _recorder = MediaRecorder(requireContext())
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        recorder.setOutputFile(recordFilePath)
        try {
            recorder.prepare()
        } catch (e: IOException) {
            Log.e(TAG, "recorder prepare() failed")
        }
        recorder.start()
    }

    private fun stopRecording() {
        recorder.stop()
        recorder.release()
        _recorder = null
    }

    private fun startPlaying() {
        _player = MediaPlayer()
        try {
            player.setDataSource(recordFilePath)
            player.prepare()
            player.start()
        } catch (e: IOException) {
            Log.e(TAG, "player prepare() failed")
        }
    }

    private fun stopPlaying() {
        player.stop()
        player.release()
        _player = null
    }
}