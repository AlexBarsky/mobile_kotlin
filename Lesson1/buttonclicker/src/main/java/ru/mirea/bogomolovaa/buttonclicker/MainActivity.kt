package ru.mirea.bogomolovaa.buttonclicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast

abstract class MainActivity : AppCompatActivity() {

    private lateinit var textViewStudent: TextView
    private lateinit var btnWhoAmI: Button
    private lateinit var btnItIsNotMe: Button
    private lateinit var checkBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewStudent = findViewById(R.id.tvOut)
        btnWhoAmI = findViewById(R.id.btnWhoAmI)
        btnItIsNotMe = findViewById(R.id.btnItIsNotMe)

        val oclBtnWhoAmI: View.OnClickListener = View.OnClickListener {
            textViewStudent.text = "My list number is 3"
            checkBox.isChecked = true
        }

        btnWhoAmI.setOnClickListener(oclBtnWhoAmI)
    }

    fun onBtnItIsNotMeClick() =
        btnItIsNotMe.setOnClickListener {
            textViewStudent.text = "It wasn`t me"
            checkBox.isChecked = false
        }

    fun onMyButtonClick() =
        Toast.makeText(this, "Another way!", Toast.LENGTH_SHORT).show()
}