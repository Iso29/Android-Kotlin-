package com.example.foodsapp.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.navigation.Navigation
import com.example.foodsapp.R
import com.example.foodsapp.data.entitiy.User
import com.example.foodsapp.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginFragment : Fragment() {
private lateinit var binding:FragmentLoginBinding
override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    binding=FragmentLoginBinding.inflate(inflater,container,false)
    val sharedPref = (activity as AppCompatActivity).getSharedPreferences("user",
        Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    binding.btnSignin.isEnabled=false
    binding.textInputLayoutEmailLogin.isHelperTextEnabled=false
    binding.textInputLayoutPasswordLogin.isHelperTextEnabled=false


    binding.inputTextEmailLogin.doOnTextChanged { text, start, before, count ->
        if(binding.editTextPasswordLogin.length().equals(0)){
            binding.textInputLayoutPasswordLogin.isHelperTextEnabled=true
            binding.textInputLayoutPasswordLogin.helperText="password can not left empty"
            binding.btnSignin.isEnabled=false
        }
        if(text!!.length>0 && binding.editTextPasswordLogin.length()>0){
            binding.btnSignin.isEnabled=true
        }
        if(text!!.length==0){
            binding.textInputLayoutEmailLogin.isHelperTextEnabled=true
            binding.textInputLayoutEmailLogin.helperText="email can not left empty"
            binding.btnSignin.isEnabled=false
        }

        else{
            binding.textInputLayoutEmailLogin.isHelperTextEnabled=false
        }
    }

    binding.editTextPasswordLogin.doOnTextChanged { text, start, before, count ->
        if(binding.inputTextEmailLogin.length().equals(0)){
            binding.textInputLayoutEmailLogin.isHelperTextEnabled=true
            binding.textInputLayoutEmailLogin.helperText="password can not left empty"
            binding.btnSignin.isEnabled=false
        }
        if(text!!.length>0 && binding.inputTextEmailLogin.length()>0){
            binding.btnSignin.isEnabled=true
        }
        if(text!!.length==0){
            binding.textInputLayoutPasswordLogin.isHelperTextEnabled=true
            binding.textInputLayoutPasswordLogin.helperText="email can not left empty"
            binding.btnSignin.isEnabled=false
        }

        else{
            binding.textInputLayoutPasswordLogin.isHelperTextEnabled=false
        }
    }
    binding.btnSignin.setOnClickListener {
        val email:String = binding.inputTextEmailLogin.text.toString().trim { it<=' '}
        val password:String= binding.editTextPasswordLogin.text.toString().trim { it<=' '}
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userID = FirebaseAuth.getInstance().currentUser!!.uid
//                val user = User(userID, email)
//                val transition = LoginFragmentDirections.fromLoginToProfile(user = user)
                editor.apply{
                    putString("userId",userID.toString())
                    putString("email",email)
                    apply()
                }
                Navigation.findNavController(it).navigate(R.id.fromLoginToProfile)
//                Navigation.findNavController(it).navigate(transition)
            } else {
                val ad = AlertDialog.Builder(requireContext())
                ad.setTitle("error !")
                ad.setMessage(task.exception!!.message.toString())
                ad.setPositiveButton("OK") { d, i ->
                }
                ad.create().show()

            }
        }
    }
    binding.textViewRegister.setOnClickListener {
        Navigation.findNavController(it).navigate(R.id.fromLoginToRegister)
    }

    return binding.root
    }
}