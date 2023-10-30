package com.lucassdalmeida.splitthebill.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.lucassdalmeida.splitthebill.controller.MEMBER_EXTRA
import com.lucassdalmeida.splitthebill.application.member.AddMemberService
import com.lucassdalmeida.splitthebill.databinding.ActivityMemberBinding
import com.lucassdalmeida.splitthebill.persistence.sqlite.SQLiteIdGenerator
import com.lucassdalmeida.splitthebill.persistence.sqlite.SQLiteMemberRepositoryImpl
import java.lang.Exception
import java.lang.IllegalArgumentException

class MemberActivity : AppCompatActivity() {
    private val activityMemberBinding by lazy { ActivityMemberBinding.inflate(layoutInflater) }
    private val addMemberService by lazy { AddMemberService(
        SQLiteMemberRepositoryImpl(this),
        SQLiteIdGenerator(this),
    )}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMemberBinding.root)
        setSupportActionBar(activityMemberBinding.activityMemberToolbar.appToolbar)

        setAddMemberListener()
    }

    private fun setAddMemberListener() = activityMemberBinding.addMemberButton.setOnClickListener {
        try {
            val name = activityMemberBinding.memberNameField.text.toString()
            val member = addMemberService.add(name)

            Intent().also {
                it.putExtra(MEMBER_EXTRA, member)
                setResult(RESULT_OK, it)
            }
            finish()
        }
        catch (error: IllegalArgumentException) {
            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
        }
        catch (error: Exception) {
            Log.e("MemberActivity", error.stackTraceToString())
        }
    }
}