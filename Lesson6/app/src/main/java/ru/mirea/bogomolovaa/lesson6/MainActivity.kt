package ru.mirea.bogomolovaa.lesson6

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.bogomolovaa.lesson6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var inputGroupNumber: EditText
    private lateinit var inputListNumber: EditText
    private lateinit var inputMovieTitle: EditText

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

        inputGroupNumber = binding.editGroupNumber
        inputListNumber = binding.editListNumber
        inputMovieTitle = binding.editMovieTitle

        sharedPreferences = getSharedPreferences("mirea_settings", MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        binding.buttonSaveSettings.setOnClickListener {
            editor.putString(GROUP_NUMBER_KEY, inputGroupNumber.text.toString())
            editor.putInt(LIST_NUMBER_KEY, inputListNumber.text.toString().toInt())
            editor.putString(MOVIE_TITLE_KEY, inputMovieTitle.text.toString())

            editor.apply()
        }
    }

    override fun onStart() {
        super.onStart()

        Log.d("TAG", "${sharedPreferences.contains(GROUP_NUMBER_KEY)}, ${sharedPreferences.contains(LIST_NUMBER_KEY)}, ${sharedPreferences.contains(MOVIE_TITLE_KEY)}")
        sharedPreferences.contains(GROUP_NUMBER_KEY).let {
            if (it) {
                inputGroupNumber.setText(sharedPreferences.getString(GROUP_NUMBER_KEY, ""))
            }
        }
        sharedPreferences.contains(LIST_NUMBER_KEY).let {
            if (it) {
                inputListNumber.setText(sharedPreferences.getInt(LIST_NUMBER_KEY, 0).toString())
            }
        }
        sharedPreferences.contains(MOVIE_TITLE_KEY).let {
            if (it) {
                inputMovieTitle.setText(sharedPreferences.getString(MOVIE_TITLE_KEY, ""))
            }
        }
    }

    private companion object {
        const val GROUP_NUMBER_KEY = "group_number"
        const val LIST_NUMBER_KEY = "list_number"
        const val MOVIE_TITLE_KEY = "movie_title"
    }
}