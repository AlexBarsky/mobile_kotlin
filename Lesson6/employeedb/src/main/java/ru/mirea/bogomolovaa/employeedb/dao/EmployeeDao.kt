package ru.mirea.bogomolovaa.employeedb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import ru.mirea.bogomolovaa.employeedb.entity.Employee

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM employees ORDER BY name ASC")
    fun getAll(): List<Employee>

    @Query("SELECT * FROM employees WHERE id = :id")
    fun getById(id: Int): Employee?

    @Insert(onConflict = REPLACE)
    fun insert(employee: Employee)

    @Update
    fun update(employee: Employee)

    @Delete
    fun delete(employee: Employee)
}