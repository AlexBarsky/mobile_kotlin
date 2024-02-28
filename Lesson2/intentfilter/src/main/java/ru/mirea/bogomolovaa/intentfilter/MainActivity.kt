package ru.mirea.bogomolovaa.intentfilter

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickGoToSite(view: View) {
        val address = "https://www.mirea.ru/"
        val openLinkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(address))
        startActivity(openLinkIntent)
    }

    fun onClickSendInfo(view: View) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "MIREA")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "BOGOMOLOV ALEKSEY ALEXANDROVICH")

        startActivity(Intent.createChooser(shareIntent, "MY FIO"))
    }
}