package com.lucassdalmeida.splitthebill.controller

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.lucassdalmeida.splitthebill.R
import com.lucassdalmeida.splitthebill.application.member.FindMembersService
import com.lucassdalmeida.splitthebill.application.member.MemberDto
import com.lucassdalmeida.splitthebill.application.member.fromDto
import com.lucassdalmeida.splitthebill.databinding.ActivityMainBinding
import com.lucassdalmeida.splitthebill.domain.model.member.Member
import com.lucassdalmeida.splitthebill.persistence.sqlite.SQLiteMemberRepositoryImpl
import com.lucassdalmeida.splitthebill.view.MainActivity
import com.lucassdalmeida.splitthebill.view.MemberActivity

class MainActivityController(
    private val mainActivity: MainActivity,
    private val activityMainBinding: ActivityMainBinding,
) {
    private val membersList = mutableListOf<Member>()
    private val membersAdapter = MembersListViewAdapter(mainActivity, membersList)
    private val memberActivityResultLauncher: ActivityResultLauncher<Intent>
    private val findMembersService = FindMembersService(SQLiteMemberRepositoryImpl(mainActivity))

    init {
        fetchAllMembers()
        activityMainBinding.membersListView.adapter = membersAdapter
        memberActivityResultLauncher = registerForMemberActivityResult()
    }

    private fun fetchAllMembers() = findMembersService.findAll()
        .forEach { membersList.add(Member.fromDto(it)) }

    private fun registerForMemberActivityResult() = mainActivity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        handleMemberIntentResult(it)
    }

    private fun handleMemberIntentResult(result: ActivityResult) {
        if (result.resultCode != RESULT_OK)
            return

        val memberDto = result.data?.getParcelableExtra<MemberDto>(MEMBER_EXTRA) ?: return
        val member = Member.fromDto(memberDto)
        val memberIndex = membersList.indexOf(member)

        if (memberIndex == -1) {
            membersList.add(member)
        }
        else {
            membersList.removeAt(memberIndex)
            membersList.add(memberIndex, member)
        }

        membersAdapter.notifyDataSetChanged()
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId != R.id.addMemberOption)
            return false
        Intent(mainActivity, MemberActivity::class.java).also {
            memberActivityResultLauncher.launch(it)
        }
        return true
    }
}
