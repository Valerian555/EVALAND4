package com.technipixl.evaland4.ui.addExpense

import android.app.AlertDialog
import android.app.DatePickerDialog
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private var expensesType: List<ExpenseType> = listOf()
    private var selectedType: ExpenseType? = null
    private var expenseDate: Long = 0

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

        //naviagtion
        binding.addToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save_button -> {

                    if (isInputValid()) {
                        //si les champs ne sont pas vide j'insère la dépense dans ma base de données
                        addExpense()
                        //je navigue vers la liste des dépenses
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

        //choix de la date
        binding.expenseDateInput.setOnClickListener {
            showDatePicker()
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

    private fun addExpense() {
        val expenseName = binding.expenseNameInput.text.toString()
        val expenseValue = binding.expenseAmountInput.text.toString().toFloat()
        val expenseType = selectedType!!.expenseTypeId

        ExpenseTypeRepository.insertExpense(name = expenseName,
            value = expenseValue,
            date = expenseDate,
            typeOfExpenseId = expenseType,
            context = requireContext())
    }

    private fun typeAlertDialog() {
        val array = expensesType.map { it.name }.toTypedArray()

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose a type")
            .setItems(array) { dialog, which ->
                    selectedType = expensesType[which]

                //changer UI boutton type
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

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { view, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, monthOfYear)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                //créer le timestamp
                val timestamp = selectedDate.timeInMillis

                //stocker le timeStamp dans ma variable
                expenseDate = timestamp

                // UI bouton date
                binding.expenseDateInput.text = timestampToDate(timestamp)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun timestampToDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }
}