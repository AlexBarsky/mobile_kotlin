package ru.mirea.bogomolovaa.thread

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.bogomolovaa.thread.databinding.ActivityMainBinding
import java.lang.Thread.currentThread

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding
    private var counter = 0

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

        val textView = binding.textView
        val thread = currentThread()

        val GROUP_NUMBER = "11-21"
        val LIST_NUMBER = 3
        val FAVORITE_MOIVE = "НИКИТА"

        textView.text = getThreadNameLine("текущего", thread.name)
        thread.name = getString(R.string.new_thread_name_text, GROUP_NUMBER, LIST_NUMBER, FAVORITE_MOIVE)
        textView.append(getThreadNameLine("нового", thread.name))

        Log.d(TAG, "Stack: ${thread.stackTrace.joinToString("\n")}")

        binding.button.setOnClickListener {
            Thread {
                val numberThread = ++counter

                Log.d("ThreadProject",
                    getString(R.string.thread_project, numberThread, GROUP_NUMBER, LIST_NUMBER))

                val endTime = System.currentTimeMillis() + 20 * 100
                while (System.currentTimeMillis() < endTime) {
                    synchronized(this) {
                        try {
                            Thread.sleep(endTime - System.currentTimeMillis())
                            Log.d(TAG, "End-time: $endTime")
                        } catch (e: Exception) {
                            throw RuntimeException(e)
                        }
                    }
                    Log.d("ThreadProject", getThreadNameLine("", "$numberThread"))
                }
            }.start()
        }

        Log.d(TAG, "Group: ${thread.threadGroup}")
    }

    private fun getThreadNameLine(threadStatus: String, threadName: String): String =
        getString(R.string.thread_name, threadStatus, threadName)
}