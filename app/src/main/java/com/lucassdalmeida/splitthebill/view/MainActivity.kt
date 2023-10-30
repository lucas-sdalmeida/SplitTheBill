package com.lucassdalmeida.splitthebill.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.lucassdalmeida.splitthebill.R
import com.lucassdalmeida.splitthebill.databinding.ActivityMainBinding
import com.lucassdalmeida.splitthebill.domain.model.member.Member

class MainActivity : AppCompatActivity() {
    private val activityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val members = mutableListOf<Member>()
    private val membersListViewAdapter by lazy { MembersListViewAdapter(this, members) }
    private lateinit var memberActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
        setSupportActionBar(activityMainBinding.mainToolbar.appToolbar)

        activityMainBinding.membersListView.adapter = membersListViewAdapter

        memberActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            Log.d("MainActivity", "Member activity has finished!")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId != R.id.addMemberOption)
            return false
        Intent(this, MemberActivity::class.java).also {
            memberActivityResultLauncher.launch(it)
        }
        return true
    }
}