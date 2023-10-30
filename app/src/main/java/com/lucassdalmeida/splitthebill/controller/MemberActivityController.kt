package com.lucassdalmeida.splitthebill.controller

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.lucassdalmeida.splitthebill.R
import com.lucassdalmeida.splitthebill.application.member.AddMemberService
import com.lucassdalmeida.splitthebill.application.member.MemberDto
import com.lucassdalmeida.splitthebill.application.member.UpdateMemberService
import com.lucassdalmeida.splitthebill.databinding.ActivityMemberBinding
import com.lucassdalmeida.splitthebill.domain.model.member.Expense
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

        with(activityMemberBinding) {
            addMemberButton.text = memberActivity.getString(R.string.edit_member_button)
            memberNameField.setText(dto.name)
            dto.expense?.let {(description, price) ->
                expenseDescriptionField.setText(description)
                expensePriceField.setText(String.format("%.2f", price))
            }
        }

        setEditMemberListener(dto.id)
    }

    private fun setAddMemberListener() = activityMemberBinding.addMemberButton.setOnClickListener {
        try {
            val name = getNameFromView()
            val expense = getExpenseFromView()

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

    private fun getExpenseFromView(): Expense? {
        val description = getExpenseDescriptionFromView()
        val price = getExpensePriceFromView()
        val expense = when {
            description.isNotBlank() && price > 0 -> Expense(description, price)
            else -> null
        }
        return expense
    }

    private fun getNameFromView() = with(activityMemberBinding) {
        memberNameField.text.toString()
    }

    private fun getExpenseDescriptionFromView() = with(activityMemberBinding) {
        expenseDescriptionField.text.toString()
    }

    private fun getExpensePriceFromView() = with(activityMemberBinding) {
        expensePriceField.text.toString().let {
            if (it.isBlank()) 0.0 else it.toDouble()
        }
    }

    private fun setEditMemberListener(id: Long) = activityMemberBinding.addMemberButton
            .setOnClickListener {
        try {
            val name = getNameFromView()
            val expense = getExpenseFromView()
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