package ru.mirea.bogomolovaa.mireaproject.ui.audiorecord

import android.Manifest.permission.RECORD_AUDIO
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mirea.bogomolovaa.mireaproject.R
import ru.mirea.bogomolovaa.mireaproject.databinding.FragmentAudioRecordBinding
import ru.mirea.bogomolovaa.mireaproject.utils.PermissionUtils
import java.io.File
import java.io.IOException

class AudioRecordFragment : Fragment() {

    companion object {
        fun newInstance() = AudioRecordFragment()
        val TAG: String = AudioRecordFragment::class.java.simpleName

        val requiredPermissions = arrayOf(
            RECORD_AUDIO,
//            WRITE_EXTERNAL_STORAGE
        )
        private const val UPDATE_INTERVAL = 100L
    }

    private val viewModel: AudioRecordViewModel by activityViewModels()
    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>

    private var _binding: FragmentAudioRecordBinding? = null
    private val binding get() = _binding!!

    private var _recorder: MediaRecorder? = null
    private val recorder: MediaRecorder get() = _recorder!!

    private var _player: MediaPlayer? = null
    private val player: MediaPlayer get() = _player!!

    private lateinit var recordButton: Button
    private lateinit var playButton: Button
    private lateinit var seekBar: SeekBar

    private lateinit var recordFilePath: String
    private var isStartRecording = true
    private var isStartPlaying = true
    private var currentPlaybackPosition = 0

    private var shouldUpdateSeekBar = false
    private var _updateJob: Job? = null

    private val updateJob: Job
        get() = synchronized(this) {
            if (_updateJob == null) {
                _updateJob = lifecycleScope.launch {
                    while (shouldUpdateSeekBar) {
                        seekBarProgressCallback.invoke()
                        delay(UPDATE_INTERVAL)
                    }
                }
            }
            return _updateJob as Job
        }

    private var seekBarProgressCallback: () -> Unit = {}

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

        recordButton = binding.recordButton
        playButton = binding.playButton
        seekBar = binding.seekBar
        playButton.isEnabled = false

        playButton.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_button_pause,
            0,
            0,
            0
        )

        recordFilePath = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            "/audiorecordtest.3gp"
        ).absolutePath

        requestPermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            PermissionUtils.handlePermissions(binding.root, permissions, R.string.audio_permissions) {
                onPermissionsGranted()
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        requestPermissionsLauncher.launch(requiredPermissions)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun onPermissionsGranted() {
        recordButton.setOnClickListener {
            when (isStartRecording) {
                true -> {
                    recordButton.text = getString(R.string.stop_recording)
                    playButton.isEnabled = false
                    seekBar.isVisible = false
                    when (_player) {
                        null -> startRecording()
                        else -> {
                            stopPlaying()
                            startRecording()
                        }
                    }
                }
                false -> {
                    recordButton.text = getString(R.string.start_recording)
                    playButton.isEnabled = true
                    seekBar.isVisible = true
                    stopRecording()
                }
            }
            isStartRecording =! isStartRecording
        }

        playButton.setOnClickListener {
            when (isStartPlaying) {
                true -> {
                    playButton.text = getString(R.string.pause_playing)
                    playButton.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_button_pause,
                        0,
                        0,
                        0
                    )
                    recordButton.isEnabled = false
                    when (_player) {
                        null -> startPlaying()
                        else -> {
                            if (currentPlaybackPosition == 0) restartPlaying()
                            else resumePlaying()
                        }
                    }
                }
                false -> {
                    playButton.text = getString(R.string.resume_playing)
                    playButton.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_button_play,
                        0,
                        0,
                        0
                    )
                    pausePlaying()
                }
            }
            isStartPlaying =! isStartPlaying
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player.seekTo(progress)
                    currentPlaybackPosition = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
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
            player.setOnCompletionListener {
                playButton.text = getString(R.string.start_playing)
                recordButton.isEnabled = true
                player.stop()
                currentPlaybackPosition = 0
                seekBar.progress = 0
                isStartPlaying =! isStartPlaying
            }
            seekBar.max = player.duration
            currentPlaybackPosition = 0

            startUpdatingSeekBar()
        } catch (e: IOException) {
            Log.e(TAG, "player prepare() failed")
        }
    }

    private fun restartPlaying() {
        stopPlaying()
        startPlaying()
    }

    private fun resumePlaying() {
        player.seekTo(currentPlaybackPosition)
        player.start()
        startUpdatingSeekBar()
    }

    private fun pausePlaying() {
        player.pause()
        stopUpdatingSeekBar()
        currentPlaybackPosition = player.currentPosition
    }

    private fun stopPlaying() {
        player.stop()
        player.release()
        currentPlaybackPosition = 0
        _player = null

        stopUpdatingSeekBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopUpdatingSeekBar()
        _recorder = null
        _player = null
        _binding = null
    }

    private fun startUpdatingSeekBar() {
        Log.d(TAG, "startUpdatingSeekBar")
        shouldUpdateSeekBar = true
        updateJob
        seekBarProgressCallback = {
            if (player.isPlaying) {
                Log.d(TAG, player.currentPosition.toString())
                seekBar.progress = player.currentPosition
                Log.d(TAG, seekBar.progress.toString())
            }
        }
    }

    private fun stopUpdatingSeekBar() {
        shouldUpdateSeekBar = false
        updateJob.cancel()
        _updateJob = null
    }
}