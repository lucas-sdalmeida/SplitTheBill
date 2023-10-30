package com.lucassdalmeida.splitthebill.controller

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.lucassdalmeida.splitthebill.R
import com.lucassdalmeida.splitthebill.application.member.FindMembersService
import com.lucassdalmeida.splitthebill.application.member.MemberDto
import com.lucassdalmeida.splitthebill.application.member.SplitTheBillService
import com.lucassdalmeida.splitthebill.application.member.fromDto
import com.lucassdalmeida.splitthebill.application.member.toDto
import com.lucassdalmeida.splitthebill.databinding.ActivityMainBinding
import com.lucassdalmeida.splitthebill.domain.model.member.Member
import com.lucassdalmeida.splitthebill.persistence.sqlite.SQLiteMemberRepositoryImpl
import com.lucassdalmeida.splitthebill.view.MainActivity
import com.lucassdalmeida.splitthebill.view.MemberActivity

class MainActivityController(
    private val mainActivity: MainActivity,
    private val activityMainBinding: ActivityMainBinding,
) {
    private val membersList = mutableListOf<Pair<Member, Double?>>()
    private val membersAdapter = MembersListViewAdapter(mainActivity, membersList)
    private val memberActivityResultLauncher: ActivityResultLauncher<Intent>
    private val findMembersService = FindMembersService(SQLiteMemberRepositoryImpl(mainActivity))
    private val splitTheBillService = SplitTheBillService(SQLiteMemberRepositoryImpl(mainActivity))

    init {
        fetchAllMembers()
        activityMainBinding.membersListView.adapter = membersAdapter
        memberActivityResultLauncher = registerForMemberActivityResult()
        setMembersListViewListener()
        setSplitTheBillListener()
    }

    private fun fetchAllMembers() = findMembersService.findAll()
        .forEach { membersList.add(Member.fromDto(it) to null) }

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
        val memberIndex = membersList.indexOfFirst { (m, _) -> m == member }

        if (memberIndex == -1) {
            membersList.add(member to null)
        }
        else {
            membersList.removeAt(memberIndex)
            membersList.add(memberIndex, member to null)
        }

        membersAdapter.notifyDataSetChanged()
    }

    private fun setMembersListViewListener() = activityMainBinding.membersListView.apply {
        setOnItemClickListener{ _, _, position, _ ->
            val (member) = membersList[position]

            Intent(mainActivity, MemberActivity::class.java).also {
                it.putExtra(ACTION_EXTRA, ActivitiesAction.EDIT)
                it.putExtra(MEMBER_EXTRA, member.toDto())
                memberActivityResultLauncher.launch(it)
            }
        }
    }
    
    private fun setSplitTheBillListener() = activityMainBinding.splitTheBillButton
            .setOnClickListener{ splitTheBill() }
    
    private fun splitTheBill() {
        if (membersList.isEmpty()) {
            Toast.makeText(
                mainActivity,
                mainActivity.getString(R.string.split_the_bill_error_toast),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val membersDeviation = splitTheBillService.split()

        membersList.clear()
        membersDeviation.map { (dto, deviation) -> Member.fromDto(dto) to deviation }
            .forEach { membersList.add(it) }

        membersAdapter.notifyDataSetChanged()
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId != R.id.addMemberOption)
            return false
        Intent(mainActivity, MemberActivity::class.java).also {
            it.putExtra(ACTION_EXTRA, ActivitiesAction.CREATE)
            memberActivityResultLauncher.launch(it)
        }
        return true
    }
}
