package com.technipixl.evaland4.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import javax.annotation.processing.Generated

@Entity
data class Expense(
    @PrimaryKey(autoGenerate = true)
    var expenseId: Long = 0,
    var date: Long,
    var name: String,
    var value: Float,
    var typeOfExpenseId: Long
)
