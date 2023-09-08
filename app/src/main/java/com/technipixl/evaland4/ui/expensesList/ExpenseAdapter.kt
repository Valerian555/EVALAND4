package com.technipixl.evaland4.ui.expensesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technipixl.evaland4.databinding.ExpensesListCellBinding
import com.technipixl.evaland4.model.Expense

class ExpenseAdapter(private val expense: List<Expense>)
    : RecyclerView.Adapter<ExpenseViewHolder>() {
    private lateinit var binding: ExpensesListCellBinding

    //responsable de la création de chaque cellule dans la recyclerView
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpenseViewHolder {
        binding = ExpensesListCellBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.setup(expense[position])
    }

    //retourne le nbr d'élément à afficher
    override fun getItemCount(): Int {
        return expense.size
    }
}