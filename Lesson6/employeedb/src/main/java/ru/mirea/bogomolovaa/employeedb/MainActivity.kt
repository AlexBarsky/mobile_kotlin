package ru.mirea.bogomolovaa.employeedb

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.bogomolovaa.employeedb.databinding.ActivityMainBinding
import ru.mirea.bogomolovaa.employeedb.entity.Employee

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = App.getInstance().getDatabase()
        val employeeDao = db.employeeDao()


        var employee = Employee()
        employee.id = 1
        employee.name = "John Smith"
        employee.salary = 10000

        employeeDao?.insert(employee)

        val employees = employeeDao?.getAll()
        employees?.forEach { Log.d("Employees", "${it.name}, ${it.salary}") }

        employee = employeeDao?.getById(1)!!
        employee.salary = 20000
        employeeDao.update(employee)
        Log.d("Employee", "${employee.name} ${employee.salary}")
    }
}