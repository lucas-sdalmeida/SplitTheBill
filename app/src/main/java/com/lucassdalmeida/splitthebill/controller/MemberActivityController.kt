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
        }

        setEditMemberListener(dto.id)
    }

    private fun setAddMemberListener() = activityMemberBinding.addMemberButton.setOnClickListener {
        try {
            val name = getNameFromView()
            val expenseDescription = getExpenseDescriptionFromView()
            val expensePrice = getExpensePriceFromView()

            val member = addMemberService.add(name, Expense(expenseDescription, expensePrice))

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
            val member = updateMemberService.rename(id, name)

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
}