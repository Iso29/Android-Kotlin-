package com.example.wpclone.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wpclone.R
import com.example.wpclone.ui.adapter.CallListAdapter
import com.example.wpclone.databinding.FragmentCallBinding
import com.example.wpclone.model.CallList

class CallFragment : Fragment() {
    private lateinit var binding : FragmentCallBinding
    private lateinit var callList:ArrayList<CallList>
    private lateinit var callAdapter : CallListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCallBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun init(){
        callAdapter = CallListAdapter(requireContext(),callList)
        binding.apply {
            callsRv.adapter = callAdapter
        }
    }
}