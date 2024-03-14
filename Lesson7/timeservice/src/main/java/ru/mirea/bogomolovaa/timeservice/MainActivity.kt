package ru.mirea.bogomolovaa.timeservice

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mirea.bogomolovaa.timeservice.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private companion object {
        val TAG: String = MainActivity::class.java.simpleName
        const val HOST = "time.nist.gov"
        const val PORT = 13
    }

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

        binding.button.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                getTimeFromServer()
            }

        }
    }


    private suspend fun getTimeFromServer() {
        try {
            val socket = Socket(HOST, PORT)
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            reader.readLine()
            val timeResult = reader.readLine()
            socket.close()

            Log.d(TAG, timeResult)
            withContext(Dispatchers.Main) {
                binding.textView.text = parseTimeResult(timeResult)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun parseTimeResult(timeResult: String): String {
        //60382 24-03-13 18:01:15 50 0 0 456.3 UTC(NIST)*
        val split = timeResult.split(" ")
        if (split.size >= 3) {
            val date = split[1]
            val time = split[2]
            return "Date: $date\nTime: $time"
        }
        return "Unable to parse time result"
    }
}