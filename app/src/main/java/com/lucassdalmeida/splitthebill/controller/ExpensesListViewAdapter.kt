package com.lucassdalmeida.splitthebill.controller

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.lucassdalmeida.splitthebill.R
import com.lucassdalmeida.splitthebill.databinding.ExpenseTileBinding
import com.lucassdalmeida.splitthebill.domain.model.member.Expense

class ExpensesListViewAdapter(
    private val context: Context,
    private val expenses: MutableList<Expense>,
): ArrayAdapter<Expense>(context, EXPENSE_TILE_LAYOUT,expenses) {
    companion object {
        val EXPENSE_TILE_LAYOUT = R.layout.expense_tile
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val expense = expenses[position]
        val targetView = convertView ?: createView()

        with(targetView.tag as ExpenseTileHolder) {
            expenseDescriptionView.text = expense.description
            expensePriceView.text = context.getString(
                R.string.price_format,
                String.format("%.2f", expense.price)
            )
        }

        return targetView
    }

    private fun createView(): View {
        val layoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val expenseTileBinding = ExpenseTileBinding.inflate(layoutInflater)
        val holder = expenseTileBinding.run {
            ExpenseTileHolder(expenseDescriptionView, expensePriceView)
        }

        return expenseTileBinding.root.also {
            it.tag = holder
        }
    }

    private data class ExpenseTileHolder(
        val expenseDescriptionView: TextView,
        val expensePriceView: TextView,
    )
}