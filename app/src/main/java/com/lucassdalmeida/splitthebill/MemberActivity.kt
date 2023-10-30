package com.lucassdalmeida.splitthebill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lucassdalmeida.splitthebill.databinding.ActivityMemberBinding

class MemberActivity : AppCompatActivity() {
    private val activityMemberBinding by lazy { ActivityMemberBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMemberBinding.root)
        setSupportActionBar(activityMemberBinding.activityMemberToolbar.appToolbar)
    }
}