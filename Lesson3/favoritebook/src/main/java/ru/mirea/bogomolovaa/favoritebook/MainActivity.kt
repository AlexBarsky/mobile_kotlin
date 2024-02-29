package ru.mirea.bogomolovaa.favoritebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var textViewUserBook: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewUserBook = findViewById(R.id.textViewBook)

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val userBook = data?.getStringExtra(USER_MESSAGE)
                    textViewUserBook.text = userBook
                }
            }
    }

    fun getInfoAboutBook(view: View) {
        val intent = Intent(this, ShareActivity::class.java)
        intent.putExtra(KEY, "Anabasis")
        activityResultLauncher.launch(intent)
    }

    companion object {
        const val KEY = "book-name"
        const val USER_MESSAGE = "MESSAGE"
    }
}