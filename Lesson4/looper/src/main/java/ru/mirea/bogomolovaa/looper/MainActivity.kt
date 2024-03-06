package ru.mirea.bogomolovaa.looper

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.bogomolovaa.looper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding

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

//        fromExampleCode()
        fromTaskCode()
    }

    private fun fromTaskCode() {
        val mainThreadHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                Log.d(TAG, "Task executed. ${msg.data.getString("result")}")
            }
        }

        val myLopper = MyLooper(mainThreadHandler)
        myLopper.start()

        binding.button.setOnClickListener {
            val startTime = System.currentTimeMillis()

            val ageText = binding.editTextAge.text.toString().toIntOrNull() ?: 0
            val positionText = binding.editTextPosition.text.toString()

            val msg = Message.obtain()
            val bundle = Bundle()

            bundle.putLong("START_TIME", startTime)
            bundle.putInt("AGE", ageText)
            bundle.putString("POSITION", positionText)
            msg.data = bundle

            myLopper.mHandler.sendMessage(msg)
        }
    }

    private fun fromExampleCode() {
        val mainThreadHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                Log.d(TAG, "Task executed. This is result: ${msg.data.getString("result")}")
            }
        }

        val myLopper = MyLooper(mainThreadHandler)
        myLopper.start()

        binding.textView.text = "Мой номер по списку № 3"
        binding.button.setOnClickListener {
            val msg = Message.obtain()
            val bundle = Bundle()
            bundle.putString("KEY", "mirea")
            msg.data = bundle
            myLopper.mHandler.sendMessage(msg)
        }
    }
}