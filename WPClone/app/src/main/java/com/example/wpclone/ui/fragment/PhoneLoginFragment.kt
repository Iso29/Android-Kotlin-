package com.example.wpclone.ui.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.wpclone.R
import com.example.wpclone.ui.activity.MainActivity
import com.example.wpclone.ui.adapter.CustomSpinnerAdapter
import com.example.wpclone.databinding.FragmentPhoneLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class PhoneLoginFragment : Fragment() {
    private lateinit var binding : FragmentPhoneLoginBinding
    private lateinit var customSpinnerAdapter: CustomSpinnerAdapter
    private val country = arrayListOf("India","USA","China","Japan","Turkey","Azerbaijan","Russia","Other")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoneLoginBinding.inflate(inflater,container,false)
        init()

        return binding.root
    }

   private fun init(){
        binding.apply {
            customSpinnerAdapter = CustomSpinnerAdapter(requireContext(),country)
            countrySpinner.adapter = customSpinnerAdapter
//            spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    TODO("Not yet implemented")
//                }
//            }
//            mAuth = FirebaseAuth.getInstance()
//            progresDialog = ProgressDialog(requireContext())

            nextButton.setOnClickListener {
                val phoneNumber = "+"+ editTextPhonePrefix.text.toString()+ editTextInputNumber.text.toString()
                if(!phoneNumber.isEmpty()){
                    val intent = PhoneLoginFragmentDirections.fromLoginToOTP(phoneNumber)
                    Navigation.findNavController(it).navigate(intent)
                }

            }
        }
    }

}