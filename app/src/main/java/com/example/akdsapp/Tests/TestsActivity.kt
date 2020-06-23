package com.example.akdsapp.Tests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.akdsapp.R
import com.example.akdsapp.utils.BottomnavigationViewHelper
import kotlinx.android.synthetic.main.activity_main.*

class TestsActivity : AppCompatActivity() {
    private val ACTİVİTY_NO = 1
    private val TAG = "HomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tests)
        setupNavigationView()
    }

    fun setupNavigationView() {
        BottomnavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomnavigationViewHelper.setupNavigation(this, bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTİVİTY_NO)
        menuItem.setChecked(true)
    }
}