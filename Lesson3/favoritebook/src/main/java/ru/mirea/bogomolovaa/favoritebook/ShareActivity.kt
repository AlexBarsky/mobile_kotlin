package ru.mirea.bogomolovaa.favoritebook

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import ru.mirea.bogomolovaa.favoritebook.MainActivity.Companion.KEY
import ru.mirea.bogomolovaa.favoritebook.MainActivity.Companion.USER_MESSAGE

class ShareActivity : AppCompatActivity() {

    private lateinit var textViewDevBook: TextView
    private lateinit var textUserFavoriteBook: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        textViewDevBook = findViewById(R.id.textViewDevBook)
        textUserFavoriteBook = findViewById(R.id.textUserFavoriteBook)

        textViewDevBook.text = "Oops! Something went wrong!"

        // Get data from MainActivity
        val extras = intent.extras
        if (extras!= null) {
            val textBook = extras.getString(KEY)
            textViewDevBook.text = "Developer`s favorite book - $textBook"
        }
    }

    fun onBtnSendClick(view: View) {
        // Send data to MainActivity
        val data = Intent()
        data.putExtra(USER_MESSAGE, textUserFavoriteBook.text.toString())
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}