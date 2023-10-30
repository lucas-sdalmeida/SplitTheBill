package com.lucassdalmeida.splitthebill.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lucassdalmeida.splitthebill.controller.MemberActivityController
import com.lucassdalmeida.splitthebill.databinding.ActivityMemberBinding

class MemberActivity : AppCompatActivity() {
    private val activityMemberBinding by lazy { ActivityMemberBinding.inflate(layoutInflater) }
    private lateinit var controller: MemberActivityController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMemberBinding.root)
        setSupportActionBar(activityMemberBinding.activityMemberToolbar.appToolbar)

        controller = MemberActivityController(this, activityMemberBinding)
    }
}