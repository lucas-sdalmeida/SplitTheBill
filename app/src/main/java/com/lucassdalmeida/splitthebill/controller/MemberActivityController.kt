package com.lucassdalmeida.splitthebill.controller

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.core.view.children
import com.lucassdalmeida.splitthebill.R
import com.lucassdalmeida.splitthebill.application.member.AddMemberService
import com.lucassdalmeida.splitthebill.application.member.MemberDto
import com.lucassdalmeida.splitthebill.application.member.UpdateMemberService
import com.lucassdalmeida.splitthebill.application.member.fromDto
import com.lucassdalmeida.splitthebill.databinding.ActivityMemberBinding
import com.lucassdalmeida.splitthebill.domain.model.member.Expense
import com.lucassdalmeida.splitthebill.domain.model.member.Member
import com.lucassdalmeida.splitthebill.persistence.inmemory.InMemoryIdGenerator
import com.lucassdalmeida.splitthebill.persistence.inmemory.InMemoryMemberRepository
import com.lucassdalmeida.splitthebill.persistence.sqlite.SQLiteIdGenerator
import com.lucassdalmeida.splitthebill.persistence.sqlite.SQLiteMemberRepositoryImpl
import com.lucassdalmeida.splitthebill.view.MemberActivity

class MemberActivityController(
    private val memberActivity: MemberActivity,
    private val activityMemberBinding: ActivityMemberBinding,
) {
    private val addMemberService by lazy { AddMemberService(
        SQLiteMemberRepositoryImpl(memberActivity),
        SQLiteIdGenerator(memberActivity),
    )}
    private val updateMemberService by lazy {
        UpdateMemberService(SQLiteMemberRepositoryImpl(memberActivity))
    }

    init {
        setUpView()
    }

    private fun setUpView() {
        val type = memberActivity.intent.getSerializableExtra(ACTION_EXTRA) as ActivitiesAction

        if (type == ActivitiesAction.CREATE) {
            setAddMemberListener()
            return
        }

        val dto = memberActivity.intent.getParcelableExtra<MemberDto>(MEMBER_EXTRA) ?: return
        val member = Member.fromDto(dto)

        with(activityMemberBinding) {
            addMemberButton.text = memberActivity.getString(R.string.edit_member_button)
            memberNameField.setText(member.name)
            putExpensesOnView(member.expenses)
        }

        setEditMemberListener(dto.id)
    }

    private fun setAddMemberListener() = activityMemberBinding.addMemberButton.setOnClickListener {
        try {
            val name = getNameFromView()
            val expense = getExpensesFromView()

            val member = addMemberService.add(name, expense)

            Intent().also {
                it.putExtra(MEMBER_EXTRA, member)
                memberActivity.setResult(RESULT_OK, it)
            }

            memberActivity.finish()
        }
        catch (error: IllegalArgumentException) {
            Toast.makeText(memberActivity, error.message, Toast.LENGTH_SHORT).show()
        }
        catch (error: Exception) {
            Log.e("MemberActivity", error.stackTraceToString())
        }
    }

    private fun getExpensesFromView(): Set<Expense> {
        val expenses = mutableSetOf<Expense>()

        getExpense1()?.let { expenses.add(it) }
        getExpense2()?.let { expenses.add(it) }
        getExpense3()?.let { expenses.add(it) }

        return expenses
    }

    private fun getExpense1(): Expense? {
        val description = activityMemberBinding.expenseDescriptionField1.text.toString()
        val price = activityMemberBinding.expensePriceField1.text.toString()

        if (description.isBlank() || price.isBlank()) return null

        return Expense(description, price.toDouble())
    }

    private fun getExpense2(): Expense? {
        val description = activityMemberBinding.expenseDescriptionField2.text.toString()
        val price = activityMemberBinding.expensePriceField2.text.toString()

        if (description.isBlank() || price.isBlank()) return null

        return Expense(description, price.toDouble())
    }

    private fun getExpense3(): Expense? {
        val description = activityMemberBinding.expenseDescriptionField3.text.toString()
        val price = activityMemberBinding.expensePriceField3.text.toString()

        if (description.isBlank() || price.isBlank()) return null

        return Expense(description, price.toDouble())
    }

    private fun getNameFromView() = with(activityMemberBinding) {
        memberNameField.text.toString()
    }

    private fun putExpensesOnView(expenses: Set<Expense>) {
        val views = with(activityMemberBinding) {listOf(
            expenseDescriptionField1 to expensePriceField1,
            expenseDescriptionField2 to expensePriceField2,
            expenseDescriptionField2 to expensePriceField2,
        )}

        expenses.forEachIndexed { index, expense -> views[index].apply {
            first.setText(expense.description)
            second.setText(String.format("%.2f", expense.price))
        }}
    }

    private fun setEditMemberListener(id: Long) = activityMemberBinding.addMemberButton
            .setOnClickListener {
        try {
            val name = getNameFromView()
            val expense = getExpensesFromView()
            val member = updateMemberService.rename(id, name, expense)

            Intent().also {
                it.putExtra(MEMBER_EXTRA, member)
                memberActivity.setResult(RESULT_OK, it)
            }

            memberActivity.finish()
        }
        catch (error: IllegalArgumentException) {
            Toast.makeText(memberActivity, error.message, Toast.LENGTH_SHORT).show()
        }
        catch (error: Exception) {
            Log.e("MemberActivity", error.stackTraceToString())
        }
    }
}