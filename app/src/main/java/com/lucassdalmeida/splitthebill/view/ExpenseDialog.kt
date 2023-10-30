package com.lucassdalmeida.splitthebill.view

import android.app.Dialog
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.Window
import com.lucassdalmeida.splitthebill.databinding.ExpenseDialogBinding
import com.lucassdalmeida.splitthebill.domain.model.member.Expense

class ExpenseDialog(
    context: Context,
) {
    private val expenseDialogBinding: ExpenseDialogBinding
    private val dialog = Dialog(context)

    init {
        val layoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        expenseDialogBinding = ExpenseDialogBinding.inflate(layoutInflater)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(expenseDialogBinding.root)
    }

    fun show() = dialog.show()
}