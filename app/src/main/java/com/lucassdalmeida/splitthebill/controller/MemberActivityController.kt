package com.lucassdalmeida.splitthebill.controller

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.lucassdalmeida.splitthebill.R
import com.lucassdalmeida.splitthebill.application.member.AddMemberService
import com.lucassdalmeida.splitthebill.application.member.MemberDto
import com.lucassdalmeida.splitthebill.application.member.RenameMemberService
import com.lucassdalmeida.splitthebill.databinding.ActivityMemberBinding
import com.lucassdalmeida.splitthebill.domain.model.member.Expense
import com.lucassdalmeida.splitthebill.persistence.sqlite.SQLiteIdGenerator
import com.lucassdalmeida.splitthebill.persistence.sqlite.SQLiteMemberRepositoryImpl
import com.lucassdalmeida.splitthebill.view.ExpenseDialog
import com.lucassdalmeida.splitthebill.view.MemberActivity

class MemberActivityController(
    private val memberActivity: MemberActivity,
    private val activityMemberBinding: ActivityMemberBinding,
) {
    private val expensesList = mutableListOf<Expense>()
    private val expensesListViewAdapter = ExpensesListViewAdapter(memberActivity, expensesList)
    private val addMemberService by lazy { AddMemberService(
        SQLiteMemberRepositoryImpl(memberActivity),
        SQLiteIdGenerator(memberActivity),
    )}
    private val renameMemberService by lazy {
        RenameMemberService(SQLiteMemberRepositoryImpl(memberActivity))
    }

    init {
        setUpView()
        setAddExpenseListener()

        activityMemberBinding.expensesListView.adapter = expensesListViewAdapter
    }

    private fun setUpView() {
        val type = memberActivity.intent.getSerializableExtra(ACTION_EXTRA) as ActivitiesAction

        if (type == null || type == ActivitiesAction.CREATE) {
            setAddMemberListener()
            return
        }

        val dto = memberActivity.intent.getParcelableExtra<MemberDto>(MEMBER_EXTRA) ?: return

        with(activityMemberBinding) {
            addMemberButton.text = memberActivity.getString(R.string.edit_member_button)
            memberNameField.setText(dto.name)
        }

        setEditMemberListener(dto.id)
    }

    private fun setAddMemberListener() = activityMemberBinding.addMemberButton.setOnClickListener {
        try {
            val name = activityMemberBinding.memberNameField.text.toString()
            val member = addMemberService.add(name)

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

    private fun setEditMemberListener(id: Long) = activityMemberBinding.addMemberButton
            .setOnClickListener {
        try {
            val name = activityMemberBinding.memberNameField.text.toString()
            val member = renameMemberService.rename(id, name)

            Log.d("MemberActivity", member.toString())

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

    private fun setAddExpenseListener() = activityMemberBinding.addExpenseOption.setOnClickListener{
        ExpenseDialog(memberActivity).also { it.show() }
    }
}