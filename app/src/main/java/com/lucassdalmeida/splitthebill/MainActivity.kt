package com.lucassdalmeida.splitthebill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.lucassdalmeida.splitthebill.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val activityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
        setSupportActionBar(activityMainBinding.mainToolbar.appToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}