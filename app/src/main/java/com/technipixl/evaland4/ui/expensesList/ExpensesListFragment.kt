package com.technipixl.evaland4.ui.expensesList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technipixl.evaland4.R
import com.technipixl.evaland4.database.ExpenseTypeDatabase
import com.technipixl.evaland4.database.ExpenseTypeRepository
import com.technipixl.evaland4.databinding.FragmentExpensesListBinding
import com.technipixl.evaland4.model.Expense
import com.technipixl.evaland4.model.ExpenseType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ExpensesListFragment : Fragment() {
    private lateinit var binding: FragmentExpensesListBinding
    private var expensesList: MutableList<Expense> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpensesListBinding.inflate(layoutInflater)

        //gestion de la toolbar
        binding.listToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_button -> {
                    findNavController().navigate(ExpensesListFragmentDirections.actionExpensesListFragmentToAddFragment())
                    true
                }
                else -> false
            }
        }

        //texte si la liste est vide
        if (expensesList.isNullOrEmpty()) {
            binding.emptyList.visibility = View.VISIBLE
        } else {
            binding.emptyList.visibility = View.INVISIBLE
        }

        ExpenseTypeRepository.getAllExpenseType(requireContext())

        //injecter les données de la db dans la liste
        loadExpensesFromDatabase()

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        //récupération du composant RecyclerView
        val recyclerView = binding.expensesRecyclerview

        //definition de son type d'affichage
        recyclerView.layoutManager = LinearLayoutManager(
            requireActivity(), RecyclerView.VERTICAL, false)

        recyclerView.adapter = ExpenseAdapter(expensesList)
    }

    //récupérer la liste des différentes dépenses
    private fun loadExpensesFromDatabase() {
        CoroutineScope(IO).launch {
            ExpenseTypeRepository.getAllExpense(requireContext()).collect {
                //effacer le contenu de la liste afin d'éviter les doublons
                expensesList.clear()

                //ajouter le contenu de la table dépense dans la liste
                expensesList.addAll(it)
            }
        }
    }
}