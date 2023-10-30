package com.lucassdalmeida.splitthebill.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.lucassdalmeida.splitthebill.R
import com.lucassdalmeida.splitthebill.controller.MainActivityController
import com.lucassdalmeida.splitthebill.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val activityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var controller: MainActivityController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
        setSupportActionBar(activityMainBinding.mainToolbar.appToolbar)

        controller = MainActivityController(this, activityMainBinding)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = controller.onOptionsItemSelected(item)
}