package ru.mirea.bogomolovaa.dialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickShowDialog(view: View) =
        AlertDialogFragment().show(supportFragmentManager, "mirea")

    fun onOkClicked() =
        Toast.makeText(applicationContext, "U`ve chosen button \"Going further\"!",
            Toast.LENGTH_SHORT).show()

    fun onNeutralClicked() =
        Toast.makeText(applicationContext, "U`ve chosen button \"On pause\"!",
            Toast.LENGTH_SHORT).show()

    fun onCancelClicked() =
        Toast.makeText(applicationContext, "U`ve chosen button \"No\"!",
            Toast.LENGTH_SHORT).show()
}