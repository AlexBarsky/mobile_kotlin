package com.example.buttonclicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var textViewStudent: TextView
    private lateinit var btnWhoAmi: Button
    private lateinit var btnItIsNotMe: Button
    private lateinit var checkBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewStudent = findViewById(R.id.tvOut)
        btnWhoAmi = findViewById(R.id.btnWhoAmI)
        btnItIsNotMe = findViewById(R.id.btnItIsNotMe)
        checkBox = findViewById(R.id.checkBox)

        val oclBtnWhoAmI: View.OnClickListener = View.OnClickListener {
            textViewStudent.text = "My list number is 3"
            checkBox.isChecked = true
        }

        btnWhoAmi.setOnClickListener(oclBtnWhoAmI)
    }

    fun onBtnItIsNotMeClicked(view: View) =
        btnItIsNotMe.setOnClickListener {
            textViewStudent.text = "It wasn`t me"
            checkBox.isChecked = false
        }

    fun onMyButtonClick(view: View) =
        Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show()
}