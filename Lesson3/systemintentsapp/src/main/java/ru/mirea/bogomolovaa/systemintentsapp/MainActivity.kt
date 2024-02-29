package ru.mirea.bogomolovaa.systemintentsapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.startActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickCall(view: View) =
        startActivity(Intent(
            Intent.ACTION_DIAL,
            Uri.parse("tel:12345678910")
        ))

    fun onClickOpenBrowser(view: View) =
        startActivity(Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://github.com")
        ))

    fun onClickOpenMaps(view: View) =
        startActivity(Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:0,0?q=Moscow,Russia")
        ))
}