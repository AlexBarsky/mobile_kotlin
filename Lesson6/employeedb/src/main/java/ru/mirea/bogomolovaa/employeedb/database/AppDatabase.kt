package ru.mirea.bogomolovaa.employeedb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mirea.bogomolovaa.employeedb.dao.EmployeeDao
import ru.mirea.bogomolovaa.employeedb.entity.Employee

@Database(entities = [Employee::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao?
}