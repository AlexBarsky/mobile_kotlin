package ru.mirea.bogomolovaa.sharer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onShareClick(view: View) =
        startActivity(Intent(this, ShareActivity::class.java))

    fun onPickDataClick(view: View) =
        startActivity(Intent(this, PickDataActivity::class.java))
}