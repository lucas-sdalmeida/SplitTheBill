package com.lucassdalmeida.splitthebill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.lucassdalmeida.splitthebill.databinding.ActivityMainBinding
import com.lucassdalmeida.splitthebill.domain.model.member.Member

class MainActivity : AppCompatActivity() {
    private val activityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val members = mutableListOf<Member>()
    private val membersListViewAdapter by lazy { MembersListViewAdapter(this, members) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
        setSupportActionBar(activityMainBinding.mainToolbar.appToolbar)

        activityMainBinding.membersListView.adapter = membersListViewAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}