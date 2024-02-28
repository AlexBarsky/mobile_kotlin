package ru.mirea.bogomolovaa.control_lesson1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myTextView: TextView = findViewById(R.id.textView)
        myTextView.text = "New text in MIREA"

        val button: Button = findViewById(R.id.main_button)
        button.text = "MireaButton"

        val checkBox: CheckBox = findViewById(R.id.checkBox)
        checkBox.isChecked = true
    }
}