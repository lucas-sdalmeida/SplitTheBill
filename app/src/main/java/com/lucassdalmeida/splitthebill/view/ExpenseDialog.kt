package com.lucassdalmeida.splitthebill.view

import android.app.Dialog
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import com.lucassdalmeida.splitthebill.databinding.ExpenseDialogBinding
import com.lucassdalmeida.splitthebill.domain.model.member.Expense
import java.lang.Exception

class ExpenseDialog(
    private val context: Context,
) {
    var expense: Expense? = null
        private set
    private val expenseDialogBinding: ExpenseDialogBinding
    private val dialog = Dialog(context)

    init {
        val layoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        expenseDialogBinding = ExpenseDialogBinding.inflate(layoutInflater)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(expenseDialogBinding.root)

        setAddExpenseListener()
    }

    fun show() = dialog.show()

    fun setOnDismissListener(callback: () -> Unit) = dialog.setOnDismissListener { callback() }

    private fun setAddExpenseListener() = expenseDialogBinding.addExpenseButton.setOnClickListener {
        try {
            expense = getExpenseFromView()
            Log.d("MemberActivity", expense.toString())
            dialog.dismiss()
        }
        catch (error: IllegalArgumentException) {
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
        catch (error: Exception) {
            Log.e("MemberActivity", error.stackTraceToString())
        }
    }

    private fun getExpenseFromView() = with(expenseDialogBinding) {
        Expense(
            expenseDescriptionField.text.toString(),
            expensePriceField.text.toString().toDouble(),
        )
    }
}