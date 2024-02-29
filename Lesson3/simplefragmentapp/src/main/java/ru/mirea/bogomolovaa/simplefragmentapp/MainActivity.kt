package ru.mirea.bogomolovaa.simplefragmentapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    lateinit var firstFragment: Fragment
    lateinit var secondFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstFragment = FirstFragment()
        secondFragment = SecondFragment()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnFirstFragment -> {
                supportFragmentManager.beginTransaction()
                  .replace(R.id.fragmentContainer, firstFragment)
                  .commit()
            }
            R.id.btnSecondFragment -> {
                supportFragmentManager.beginTransaction()
                  .replace(R.id.fragmentContainer, secondFragment)
                  .commit()
            }
            else -> {  }
        }
    }
}