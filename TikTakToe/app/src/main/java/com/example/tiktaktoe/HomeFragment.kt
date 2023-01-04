package com.example.tiktaktoe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.Navigation
import com.example.tiktaktoe.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        binding.btnPlay.isEnabled=false
        binding.radioButtonPVP.isChecked=true

        binding.radioButtonPVP.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                binding.inputLayoutUser2.visibility=View.VISIBLE
            }
        }

        binding.radioButtonPVC.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                binding.inputLayoutUser2.visibility=View.GONE
            }
        }

        binding.editTextUser1.doOnTextChanged { text, start, before, count ->
            if(binding.editTextUser1.length()>0){
                binding.inputLayoutUser1.error=null
                if(binding.radioButtonPVP.isChecked) {
                    if (binding.editTextUser1.text.toString() == binding.editTextUser2.text.toString()) {
                        binding.inputLayoutUser1.error = "username can not be same"
                        binding.btnPlay.isEnabled = false
                    } else {
                        binding.inputLayoutUser1.error = null
                        if(binding.editTextUser2.length()==0){
                            binding.inputLayoutUser2.error="this field can not left empty"
                            binding.btnPlay.isEnabled=false
                        }else{
                            binding.inputLayoutUser2.error=null
                            binding.btnPlay.isEnabled = true
                        }
                    }
                }else{
                    binding.btnPlay.isEnabled=true
                }
            }
            else{
                binding.inputLayoutUser1.error="this field can not left empty"
                binding.btnPlay.isEnabled=false
            }
        }

        binding.editTextUser2.doOnTextChanged { text, start, before, count ->
            if(binding.radioButtonPVP.isChecked){
                if(binding.editTextUser1.length()==binding.editTextUser2.length() && binding.editTextUser2.length()==0){
                    binding.btnPlay.isEnabled=false
                    binding.inputLayoutUser1.error="this field can not left empty"
                    binding.inputLayoutUser2.error="this field can not left empty"
                }else{
                    if(binding.editTextUser1.text.toString() == binding.editTextUser2.text.toString()){
                        binding.btnPlay.isEnabled=false
                        binding.inputLayoutUser1.error=null
                        binding.inputLayoutUser2.error="username can not be same"
                    }else{
                        binding.btnPlay.isEnabled=true
                        binding.inputLayoutUser2.error=null
                    }
                }
            }
        }

        binding.btnPlay.setOnClickListener {
            if (binding.radioButtonPVC.isChecked){
                val user1 = binding.editTextUser1.text.toString()
                val transition = HomeFragmentDirections.fromHomeToPVC(user1=user1)
                Navigation.findNavController(it).navigate(transition)
            }
            if (binding.radioButtonPVP.isChecked){
                val user1 = binding.editTextUser1.text.toString()
                val user2 = binding.editTextUser2.text.toString()
                val transition = HomeFragmentDirections.fromHomeToPVP(user1=user1,user2=user2)
                Navigation.findNavController(it).navigate(transition)
            }
        }

        return binding.root
    }

}