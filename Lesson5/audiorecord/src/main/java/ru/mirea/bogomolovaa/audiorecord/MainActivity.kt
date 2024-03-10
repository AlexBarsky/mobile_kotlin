package ru.mirea.bogomolovaa.audiorecord

import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import ru.mirea.bogomolovaa.audiorecord.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var _recorder: MediaRecorder? = null
    private val recorder: MediaRecorder get() = _recorder!!
    private var _player: MediaPlayer? = null
    private val player: MediaPlayer get() = _player!!

    private lateinit var recordFilePath: String
    private var isWork = false
    private var isStartRecording = true
    private var isStartPlaying = true

    @RequiresApi(Build.VERSION_CODES.S)
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

        val recordButton = binding.recordButton
        val playButton = binding.playButton
        playButton.isEnabled = false

        recordFilePath = File(
            getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            "/audiorecordtest.3gp"
        ).absolutePath

        val audioRecordPermissionStatus = ContextCompat.checkSelfPermission(this, RECORD_AUDIO)
        val storagePermissionStatus = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)

        if (audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED
            && storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true
        } else {
            requestPermissions(arrayOf(RECORD_AUDIO, WRITE_EXTERNAL_STORAGE), REQUEST_CODE_AUDIO_RECORD)
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
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_AUDIO_RECORD -> {
                isWork = grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
            }
        }
        if (!isWork) {
            Snackbar.make(binding.root, getString(R.string.permission_denied), Snackbar.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun startRecording() {
        _recorder = MediaRecorder(applicationContext)
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

    private companion object {
        val TAG: String = MainActivity::class.java.simpleName
        const val REQUEST_CODE_AUDIO_RECORD = 200
    }
}