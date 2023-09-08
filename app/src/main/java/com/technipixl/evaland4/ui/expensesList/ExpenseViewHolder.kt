package com.technipixl.evaland4.ui.expensesList

import androidx.recyclerview.widget.RecyclerView
import com.technipixl.evaland4.database.ExpenseTypeRepository
import com.technipixl.evaland4.databinding.ExpensesListCellBinding
import com.technipixl.evaland4.model.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpenseViewHolder(private var binding: ExpensesListCellBinding) :
RecyclerView.ViewHolder(binding.root) {

    fun setup(expense: Expense) {
        binding.expenseName.text = expense.name
        binding.expenseDate.text = formatTimestampToDate(expense.date)
        binding.expenseValue.text = floatToEuro(expense.value)

        //afficher le type de dépense
        CoroutineScope(IO).launch {
            val typeName = getNameById(expense.typeOfExpenseId)

            withContext(Dispatchers.Main) {
                binding.expenseType.text = typeName
            }
        }
    }

    private suspend fun getNameById(typeID: Long): String {
        //withContext permet de changer le contexte d'exécution d'une coroutine.
            return withContext(IO) {
                //je récupère le première élément sinon null
                val typeName = ExpenseTypeRepository.getTypeById(typeID).firstOrNull()?.name ?: "No type"
                typeName
            }
    }

    private fun floatToEuro(value: Float): String {
        return String.format("%.2f €", value)
    }

    private fun formatTimestampToDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }
}