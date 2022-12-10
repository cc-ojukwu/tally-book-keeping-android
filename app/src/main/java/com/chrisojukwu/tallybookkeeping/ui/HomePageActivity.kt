package com.chrisojukwu.tallybookkeeping.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.databinding.ActivityHomePageBinding
import com.chrisojukwu.tallybookkeeping.ui.bookkeeping.EditIncomeFragment
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

        //set custom toolbar as ActionBar
        //setSupportActionBar(findViewById(R.id.toolbar_home_page_activity))

        //prevent phone keyboard from pushing up page
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val bottomNavView: BottomNavigationView = binding.bottomNavHomeActivity

        bottomNavView.setOnItemReselectedListener { } // prevent navigating to the same item

        //list of fragments that should be regarded as first level destinations
        //this tells the navController to remove the up navigate button from these destinations
        val appBarConfiguration = AppBarConfiguration
            .Builder(
                R.id.homeFragment,
                R.id.inventoryFragment,
                R.id.todoFragment,
                R.id.accountFragment,
                R.id.editIncomeFragment
            )
            .build()

        //setup navController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container_home_activity) as NavHostFragment

        navController = navHostFragment.navController

        //link the navController to the bottomNav for navigation
        NavigationUI.setupWithNavController(bottomNavView, navController)

        //link the appBarConfiguration to the navController
        //setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.editIncomeFragment
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
}