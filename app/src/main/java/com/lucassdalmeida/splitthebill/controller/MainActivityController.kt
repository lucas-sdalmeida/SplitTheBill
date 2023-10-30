package com.lucassdalmeida.splitthebill.controller

import android.content.Intent
import android.util.Log
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.lucassdalmeida.splitthebill.R
import com.lucassdalmeida.splitthebill.databinding.ActivityMainBinding
import com.lucassdalmeida.splitthebill.domain.model.member.Member
import com.lucassdalmeida.splitthebill.view.MainActivity
import com.lucassdalmeida.splitthebill.view.MemberActivity

class MainActivityController(
    private val mainActivity: MainActivity,
    private val activityMainBinding: ActivityMainBinding,
) {
    private val membersList = mutableListOf<Member>()
    private val membersAdapter = MembersListViewAdapter(mainActivity, membersList)
    private val memberActivityResultLauncher: ActivityResultLauncher<Intent>

    init {
        activityMainBinding.membersListView.adapter = membersAdapter
        memberActivityResultLauncher = registerForMemberActivityResult()
    }

    private fun registerForMemberActivityResult() = mainActivity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        Log.d("MainActivity", "Member activity has finished!")
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
