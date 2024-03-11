package ru.mirea.bogomolovaa.notebook

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.bogomolovaa.notebook.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets


class MainActivity : AppCompatActivity() {

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


        when {
            isExternalStorageAvailable() -> {
                binding.btnSaveData.setOnClickListener {
                    val fileName = binding.editTextFileName.text
                    val quote = binding.editTextQuote.text

                    Thread {
                        writeFileToExternalStorage(fileName.toString(), quote.toString())
                    }.start()

                    fileName.clear()
                    quote.clear()
                }
                binding.btnLoadData.setOnClickListener {
                    val fileName = binding.editTextFileName.text.toString()
                    val editTextQuote = binding.editTextQuote

                    Thread {
                        readFileFromExternalStorage(fileName)?.let {
                            Log.d(TAG, it.toString())
                            editTextQuote.post {
                                editTextQuote.setText(it.joinToString("\n"))
                            }
                        }
                    }.start()
                }
            }
            else -> {
                Toast.makeText(
                    this,
                    "External storage is not available",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isExternalStorageAvailable(): Boolean =
        Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

    private fun writeFileToExternalStorage(fileName: String, quote: String) {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val file = File(path, fileName)
        try {
            val fileOutputStream = FileOutputStream(file.absoluteFile)
            val output = OutputStreamWriter(fileOutputStream)

            output.write(quote)
            output.close()
        } catch (e: IOException) {
            Log.w(TAG, "Error writing $file", e)
        }
    }

    private fun readFileFromExternalStorage(fileName: String): MutableList<String>? {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val file = File(path, fileName)
        try {
            val fileInputStream = FileInputStream(file.getAbsoluteFile())
            val inputStreamReader = InputStreamReader(fileInputStream, StandardCharsets.UTF_8)
            val lines: MutableList<String> = ArrayList()
            val reader = BufferedReader(inputStreamReader)
            var line = reader.readLine()
            while (line != null) {
                lines.add(line)
                line = reader.readLine()
            }
            return lines
        } catch (e: Exception) {
            Log.w(
                "ExternalStorage",
                String.format("Read	from file %s failed", e.message)
            )
        }
        return null
    }

    private companion object {
        val TAG = MainActivity::class.java.simpleName
    }
}