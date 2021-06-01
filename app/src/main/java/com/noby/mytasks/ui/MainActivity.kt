package com.noby.mytasks.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.noby.mytasks.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var  navControll :NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navControll =  navHostFragment.findNavController()

        setupActionBarWithNavController(navControll)
    }


    override fun onSupportNavigateUp(): Boolean {
        return  navControll.navigateUp()   || super.onSupportNavigateUp()
    }

}

const val  ADD_Task_Result_OK =  1
const val  EDIT_Task_Result_OK =  2
