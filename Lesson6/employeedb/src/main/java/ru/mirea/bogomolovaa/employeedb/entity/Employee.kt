package ru.mirea.bogomolovaa.employeedb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "employees")
class Employee{
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name: String? = null
    var salary = 0
}