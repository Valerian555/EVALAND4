package com.technipixl.evaland4.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.technipixl.evaland4.model.Expense
import com.technipixl.evaland4.model.ExpenseType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExpenseTypeRepository {

    companion object {
        var expenseTypeDatabase: ExpenseTypeDatabase? = null


        fun initializeDB(context: Context): ExpenseTypeDatabase {
            val db = ExpenseTypeDatabase.getDb(context)
            CoroutineScope(IO).launch {
                val expenseType = db.expenseTypeDao().getAllType()
                if (expenseType.isNullOrEmpty()) {
                    db.expenseTypeDao().insert(ExpenseType(name = "Taxes"))
                    db.expenseTypeDao().insert(ExpenseType(name = "Rent"))
                    db.expenseTypeDao().insert(ExpenseType(name = "Insurances"))
                    db.expenseTypeDao().insert(ExpenseType(name = "Food"))
                    db.expenseTypeDao().insert(ExpenseType(name = "Transportation"))
                    db.expenseTypeDao().insert(ExpenseType(name = "Saving"))
                    db.expenseTypeDao().insert(ExpenseType(name = "Medical"))
                    db.expenseTypeDao().insert(ExpenseType(name = "Others"))
                }
            }
            return db
        }

        fun getAllExpenseType(context: Context): Flow<List<ExpenseType>> {
            if(expenseTypeDatabase == null) {
                expenseTypeDatabase = initializeDB(context)
            }
            return expenseTypeDatabase!!.expenseTypeDao().getAll()
        }

        fun getAllExpense(context: Context): Flow<List<Expense>> {
            if(expenseTypeDatabase == null) {
                expenseTypeDatabase = initializeDB(context)
            }
            return expenseTypeDatabase!!.expenseDao().getAll()
        }

        fun getTypeById(typeId: Long): Flow<ExpenseType>{
            return expenseTypeDatabase!!.expenseTypeDao().findById(typeId)
        }

        fun insertExpense(
            context: Context,
            name: String,
            date: Long,
            value: Float,
            typeOfExpenseId: Long,
        ) {
            expenseTypeDatabase = initializeDB(context)
            CoroutineScope(IO).launch {
                val expense = Expense(name = name, date = date, value = value, typeOfExpenseId = typeOfExpenseId)
                expenseTypeDatabase!!.expenseDao().insert(expense)
            }
        }


    }
}