package com.technipixl.evaland4.ui.addExpense

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Path.Direction
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.technipixl.evaland4.R
import com.technipixl.evaland4.database.ExpenseTypeRepository
import com.technipixl.evaland4.databinding.FragmentAddBinding
import com.technipixl.evaland4.model.ExpenseType
import com.technipixl.evaland4.ui.expensesList.ExpensesListFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private var expensesType: List<ExpenseType> = listOf()
    private var selectedType: ExpenseType? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(layoutInflater)

        //récupérer tous les types de dépenses
        CoroutineScope(IO).launch {
            ExpenseTypeRepository.getAllExpenseType(requireContext()).collect {
                expensesType = it
            }
        }

        //gestion de la toolbar
        binding.addToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save_button -> {

                    if (isInputValid()) {
                        addItem()
                        findNavController().navigateUp()
                    }
                    true
                }
                else -> false
            }
        }

        //Choix des types
        binding.expenseTypeInput.setOnClickListener {
            typeAlertDialog()
        }

        return binding.root
    }

    //change text of the type button
    private fun changeButtonText() {
        if (selectedType == null) {
            binding.expenseTypeInput.text = "Choose Type"
        } else {
            binding.expenseTypeInput.text = selectedType!!.name
        }
    }

    private fun addItem() {
        val expenseName = binding.expenseNameInput.text.toString()
        val expenseValue = binding.expenseAmountInput.text.toString().toFloat()
        val expenseDate = binding.expenseDateInput.text.toString().toLong()
        val expenseType = selectedType!!.expenseTypeId


        ExpenseTypeRepository.insertExpense(name = expenseName,
            value = expenseValue,
            date = expenseDate,
            typeOfExpenseId = expenseType,
            context = requireContext())
    }

    private fun typeAlertDialog() {
        val array = expensesType.map { it.name }.toTypedArray()

        //alertDialog choix du type
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose a type")
            .setItems(array) { dialog, which ->
                    selectedType = expensesType[which]
                changeButtonText()
                }
        builder.create().show()
    }

    private fun isInputValid(): Boolean {
        val expenseName = binding.expenseNameInput.text.toString()
        val expenseValueStr = binding.expenseAmountInput.text.toString()
        val expenseDateStr = binding.expenseDateInput.text.toString()

        if (expenseName.isNullOrEmpty()) {
            showToast("Expense name cannot be empty")
            return false
        }

        if (expenseValueStr.isBlank()) {
            showToast("Expense amount cannot be empty")
            return false
        }

        if (expenseDateStr.isBlank()) {
            showToast("Expense date cannot be empty")
            return false
        }

        if (selectedType == null) {
            showToast("Expense type must be selected")
            return false
        }

        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}