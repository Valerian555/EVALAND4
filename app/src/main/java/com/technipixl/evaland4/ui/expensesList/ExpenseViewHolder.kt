package com.technipixl.evaland4.ui.expensesList

import androidx.annotation.RestrictTo.Scope
import androidx.recyclerview.widget.RecyclerView
import com.technipixl.evaland4.database.ExpenseTypeRepository
import com.technipixl.evaland4.databinding.ExpensesListCellBinding
import com.technipixl.evaland4.model.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpenseViewHolder(private var binding: ExpensesListCellBinding) :
RecyclerView.ViewHolder(binding.root) {

    fun setup(expense: Expense) {
        binding.expenseName.text = expense.name
        binding.expenseDate.text = expense.date.toString()
        binding.expenseValue.text = floatToEuro(expense.value)
        CoroutineScope(IO).launch {
            val typeName = getNameById(expense.typeOfExpenseId)

            withContext(Dispatchers.Main) {
                binding.expenseType.text = typeName
            }
        }
    }

    suspend fun getNameById(typeID: Long): String {
            return withContext(IO) {
                val typeName = ExpenseTypeRepository.getTypeById(typeID).firstOrNull()?.name ?: "No type"
                typeName
            }
    }

    fun floatToEuro(value: Float): String {
        val formattedValue = String.format("%.2f €", value)
        return formattedValue
    }
}