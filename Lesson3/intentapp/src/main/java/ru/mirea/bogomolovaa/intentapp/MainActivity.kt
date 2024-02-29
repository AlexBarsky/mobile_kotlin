package ru.mirea.bogomolovaa.intentapp

import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickStartActivity(view: View) {
        val dateInMillis = System.currentTimeMillis()
        val format = "yyyy-MM-dd HH:mm:ss"
        val sdf = SimpleDateFormat(format, Locale.US)
        val dateString = sdf.format(Date(dateInMillis))

        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("date", dateString)
        startActivity(intent)
    }
}