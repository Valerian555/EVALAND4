package com.technipixl.evaland4.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.technipixl.evaland4.model.ExpenseType
import com.technipixl.evaland4.model.ExpenseTypeWithExpenses
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseTypeDao {
    @Query("SELECT * FROM expensetype WHERE expenseTypeId = :expenseTypeId LIMIT 1")
    fun findById(expenseTypeId: Long): Flow<ExpenseType>

    @Insert
    suspend fun insert(expenseType: ExpenseType): Long

    @Query("SELECT * FROM expensetype")
    fun getAll(): Flow<List<ExpenseType>>

    @Query("SELECT * FROM expensetype")
    fun getAllType(): List<ExpenseType>

    @Query("DELETE FROM expensetype WHERE expenseTypeId = :expenseTypeId")
    suspend fun deleteExpenseTypeById(expenseTypeId: Long)

    //jointure one to many
    @Transaction
    @Query("SELECT * FROM expensetype WHERE expenseTypeId = :expenseTypeId")
    fun getExpenseTypeWithExpensesById(expenseTypeId: Long): ExpenseTypeWithExpenses

    @Transaction
    @Query("SELECT * FROM expensetype")
    fun getExpenseTypeWithExpenses(): List<ExpenseTypeWithExpenses>

}