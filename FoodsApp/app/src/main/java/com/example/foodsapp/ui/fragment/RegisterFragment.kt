package com.example.foodsapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
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
import com.example.foodsapp.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterFragment : Fragment() {
    private lateinit var binding : FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRegisterBinding.inflate(inflater,container,false)
        val sharedPref = (activity as AppCompatActivity).getSharedPreferences("user",
            Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        binding.btnRegister.isEnabled=false
        binding.textInputLayoutEmail.isHelperTextEnabled=false
        binding.textInputLayoutPassword.isHelperTextEnabled=false

        binding.inputTextEmail.doOnTextChanged { text, start, before, count ->
            if (binding.textInputLayoutPassword.lengthCounter.equals(0)){
                binding.textInputLayoutPassword.isHelperTextEnabled=true
                binding.textInputLayoutPassword.helperText="password can not left empty"
                binding.btnRegister.isEnabled=false
            }
            if (text!!.length>0 && binding.inputTextPassword.length()>0){
                binding.btnRegister.isEnabled=true
            }
            if(text!!.length==0){
                binding.textInputLayoutEmail.isHelperTextEnabled=true
                binding.textInputLayoutEmail.helperText="email can not left empty"
                binding.btnRegister.isEnabled=false

            }
            else{
                binding.textInputLayoutEmail.isHelperTextEnabled=false
            }
        }

        binding.inputTextPassword.doOnTextChanged { text, start, before, count ->
            if (binding.textInputLayoutEmail.lengthCounter.equals(0)){
                binding.textInputLayoutEmail.isHelperTextEnabled=true
                binding.textInputLayoutEmail.helperText="email can not left empty"
                binding.btnRegister.isEnabled=false

            }
            if (text!!.length>0 && binding.inputTextEmail.length()>0){
                binding.btnRegister.isEnabled=true
            }
            if(text!!.length==0){
                binding.textInputLayoutPassword.isHelperTextEnabled=true
                binding.textInputLayoutPassword.helperText="password can not left empty"
                binding.btnRegister.isEnabled=false

            }
            else{
                binding.textInputLayoutPassword.isHelperTextEnabled=false
            }
        }

        binding.btnRegister.setOnClickListener {
            val email:String = binding.inputTextEmail.text.toString().trim { it<=' '}
            val password:String = binding.inputTextPassword.text.toString().trim { it<=' '}
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    Snackbar.make(it, "registration succesfully", Snackbar.LENGTH_SHORT).show()
//                    val user = User(firebaseUser.uid, email)
//                    val transition = RegisterFragmentDirections.fromRegisterToProfile(user = user)

                    editor.apply{
                        putString("userId",firebaseUser.toString())
                        putString("email",email)
                        apply()
                    }
                    Navigation.findNavController(it).navigate(R.id.fromRegisterToProfile)
//                    Navigation.findNavController(it).navigate(transition)
                } else {
                    val ad = AlertDialog.Builder(requireContext())
                    ad.setTitle("error!")
                    ad.setMessage(task.exception!!.message.toString())
                    ad.setPositiveButton("Yes"){
                        d,i->
                    }
                    ad.create().show()
                }
            }
        }

        binding.textViewLogin.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.fromRegisterToLogin)
        }

        return binding.root
    }

}