package ru.mirea.bogomolovaa.intentapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SecondActivity : AppCompatActivity() {

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val number = 3
        val text = "SQUARE OF MY LIST NUMBER IN GROUP IS ${number.pow(2)}, " +
                "and the current date is ${intent.getStringExtra("date")}"

        textView = findViewById(R.id.textView)
        textView.text = text
    }

    private fun Int.pow(x: Int): Int = (2..x).fold(this) { r, _ -> r * this }
}