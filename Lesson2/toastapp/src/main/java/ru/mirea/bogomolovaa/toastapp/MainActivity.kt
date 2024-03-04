package ru.mirea.bogomolovaa.toastapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var inputField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputField = findViewById(R.id.input_field)
    }

    fun onClickShowToast(view: View) {
        val message = inputField.text.toString()
        Toast.makeText(this, "STUDENT №3 GROUP BSBO-11-21 Characters count - ${message.length}", Toast.LENGTH_LONG).show()
    }
}