package com.technipixl.evaland4.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.technipixl.evaland4.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expense WHERE expenseId = :expenseId LIMIT 1")
    fun findById(expenseId: Long): Flow<Expense>

    @Insert
    suspend fun insert(expense: Expense): Long

    @Query("SELECT * FROM expense")
    fun getAll(): Flow<List<Expense>>

    @Query("DELETE FROM expense WHERE expenseId = :expenseId")
    suspend fun deleteExpenseById(expenseId: Long)
}