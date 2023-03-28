package com.example.wpclone.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wpclone.R
import com.example.wpclone.databinding.ActivityContactsBinding

class ContactsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityContactsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}