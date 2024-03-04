package ru.mirea.bogomolovaa.dialogapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickShowDateDialog(view: View) =
        MyDateDialogFragment().show(supportFragmentManager, "mirea")

    fun onClickShowProgressDialog(view: View) =
        MyProgressDialogFragment().show(supportFragmentManager, "mirea")

    fun onClickShowTimeDialog(view: View) =
        MyTimeDialogFragment().show(supportFragmentManager, "mirea")

    fun callbackTime(view: View, hour: Int, minute: Int) =
//        Toast.makeText(applicationContext, "Time selected: $hour: $minute",
//            Toast.LENGTH_LONG).show()
        Snackbar.make(findViewById(android.R.id.content), "Time selected: $hour:$minute",
            Snackbar.LENGTH_LONG).show()

    fun callbackDate(view: View, year: Int, month: Int, dayOfMonth: Int) =
//        Toast.makeText(applicationContext, "Date selected: $dayOfMonth.$month.$year",
//            Toast.LENGTH_LONG).show()
        Snackbar.make(findViewById(android.R.id.content), "Date selected: $dayOfMonth.$month.$year",
            Snackbar.LENGTH_LONG).show()
}