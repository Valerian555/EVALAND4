package com.technipixl.evaland4.model

import androidx.room.Embedded
import androidx.room.Relation


data class ExpenseTypeWithExpenses(
    @Embedded val expenseType: ExpenseType,
            @Relation(
                parentColumn = "expenseTypeId",
                entityColumn = "typeOfExpenseId"
            )
            val expenses: List<Expense>
)