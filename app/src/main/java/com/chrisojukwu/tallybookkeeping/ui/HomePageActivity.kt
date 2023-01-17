package com.chrisojukwu.tallybookkeeping.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.databinding.ActivityHomePageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomePageBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //prevent phone keyboard from pushing up page
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val bottomNavView: BottomNavigationView = binding.bottomNavHomeActivity

        bottomNavView.setOnItemReselectedListener { } // prevent navigating to the same item

        //setup navController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container_home_activity) as NavHostFragment

        navController = navHostFragment.navController

        //link the navController to the bottomNav for navigation
        NavigationUI.setupWithNavController(bottomNavView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.editIncomeFragment ||
                destination.id == R.id.editExpenseFragment ||
                destination.id == R.id.createExpenseFragment ||
                destination.id == R.id.createIncomeFragment  ||
                destination.id == R.id.incomeExpenseDetailsFragment ||
                destination.id == R.id.allRecordsFragment ||
                destination.id == R.id.receiptFragment   ||
                destination.id == R.id.addNewInventoryItemFragment
            ) {
                bottomNavView.visibility = View.GONE
            } else {
                bottomNavView.visibility = View.VISIBLE
            }
        }
    }

    //call the navController to handle up button clicks from the ActionBar
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}