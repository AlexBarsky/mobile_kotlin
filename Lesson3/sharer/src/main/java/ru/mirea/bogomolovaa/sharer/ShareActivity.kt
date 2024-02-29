package ru.mirea.bogomolovaa.sharer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class ShareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("*/*")
        intent.putExtra(Intent.EXTRA_TEXT, "Mirea")
        startActivity(Intent.createChooser(intent, "Выбор за вами!"))
    }
}