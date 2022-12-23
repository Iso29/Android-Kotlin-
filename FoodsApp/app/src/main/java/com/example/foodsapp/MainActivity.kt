package com.example.foodsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.foodsapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding :ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navContainer) as NavHostFragment

        NavigationUI.setupWithNavController(binding.bottomNav, navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id==R.id.foodDetailFragment){
                binding.bottomNav.visibility=View.GONE
            }
            if(destination.id==R.id.homeFragment){
                binding.bottomNav.visibility=View.VISIBLE
            }
            if(destination.id==R.id.cartFragment){
                binding.bottomNav.visibility=View.VISIBLE
            }
            if(destination.id==R.id.loginFragment){
                binding.bottomNav.visibility=View.GONE
            }
            if(destination.id==R.id.registerFragment){
                binding.bottomNav.visibility=View.GONE
            }
            if(destination.id==R.id.searchFragment){
                binding.bottomNav.visibility=View.GONE
            }
            if(destination.id==R.id.profileFragment){
                binding.bottomNav.visibility=View.VISIBLE
            }

        }

    }

    override fun onResume() {
        super.onResume()
    }
}