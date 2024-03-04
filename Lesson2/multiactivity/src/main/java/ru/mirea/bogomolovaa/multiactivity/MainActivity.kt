package ru.mirea.bogomolovaa.multiactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private lateinit var inputField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputField = findViewById(R.id.inputField)
    }

    fun onClickNewActivity(view: View) {
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("key", inputField.text.toString())
        startActivity(intent)
    }
}