package com.technipixl.evaland4.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExpenseType(
    @PrimaryKey(autoGenerate = true)
    var expenseTypeId: Long = 0,
    var name: String
)
