package com.technipixl.evaland4.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.technipixl.evaland4.dao.ExpenseDao
import com.technipixl.evaland4.dao.ExpenseTypeDao
import com.technipixl.evaland4.model.Expense
import com.technipixl.evaland4.model.ExpenseType

@Database(entities = [ExpenseType::class, Expense::class], version = 1)
abstract class ExpenseTypeDatabase: RoomDatabase() {

    abstract fun expenseTypeDao(): ExpenseTypeDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var sharedInstance: ExpenseTypeDatabase? = null

        fun getDb(context: Context) : ExpenseTypeDatabase {
            if (sharedInstance != null) return sharedInstance!!
            synchronized(this) {
                sharedInstance = Room
                    .databaseBuilder(context, ExpenseTypeDatabase::class.java, "expenseType.db")
                    .fallbackToDestructiveMigration()
                    .build()
                return sharedInstance!!
            }
        }
    }

}