package ru.mirea.bogomolovaa.internalfilestorage

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.bogomolovaa.internalfilestorage.databinding.ActivityMainBinding
import java.io.FileInputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var inputDate: EditText
    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inputDate = binding.editTextDate
        textViewResult = binding.textViewResult


        binding.btnCreateFile.setOnClickListener {
            Thread {
                try {
                    val date = inputDate.text.toString()
                    val outputStream = openFileOutput(FILE_NAME, MODE_PRIVATE)
                    outputStream.write(date.toByteArray())
                    outputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    override fun onResume() {
        super.onResume()

        Thread {
            try {
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            textViewResult.post {
                textViewResult.text = getTextFromFile()
            }
        }.start()
    }

    private fun getTextFromFile(): String? {
        var inputStream: FileInputStream? = null
        try {
            inputStream = openFileInput(FILE_NAME)
            val buffer = ByteArray(inputStream.available())

            inputStream.read(buffer)

            val text = String(buffer)
            Log.d(TAG, text)

            inputStream.close()
            return text
        } catch (e: IOException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show();
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show();
            }
        }
        return null
    }

    private companion object {
        const val FILE_NAME = "mirea.txt"
        val TAG = MainActivity::class.java.simpleName
    }
}