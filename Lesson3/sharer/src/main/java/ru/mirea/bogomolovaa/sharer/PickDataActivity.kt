package ru.mirea.bogomolovaa.sharer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class PickDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_data)

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "*/*" //"image/*"

        val imageActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    Log.d(MainActivity::class.simpleName, "Data: ${data?.dataString}")
                }
            }

        imageActivityResultLauncher.launch(intent)
    }
}