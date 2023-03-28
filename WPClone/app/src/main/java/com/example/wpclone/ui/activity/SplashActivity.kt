package com.example.wpclone.ui.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.NavHostFragment
import com.example.wpclone.R
import com.example.wpclone.databinding.ActivitySplashBinding
import com.example.wpclone.preferanceManager.PreferanceManager
import com.example.wpclone.utils.Constants

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_WPClone)
        setContentView(binding.root)
        val preferanceManager = PreferanceManager(this)
        if (preferanceManager.getBoolean(Constants.isSignedIn)){
            if(preferanceManager.getString(Constants.userName)!=null){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else{
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                navHostFragment.navController.navigate(R.id.setUserInfoFragment)
            }
        }

    }
}