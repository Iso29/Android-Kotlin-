package com.example.foodsapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.foodsapp.R
import com.example.foodsapp.data.entitiy.CartFood
import com.example.foodsapp.data.entitiy.Food
import com.example.foodsapp.databinding.FragmentCartBinding
import com.example.foodsapp.ui.adapter.CartAdapter
import com.example.foodsapp.ui.adapter.FoodsAdapter
import com.example.foodsapp.ui.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding :FragmentCartBinding
    private lateinit var viewModel:CartViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCartBinding.inflate(inflater,container,false)
        val sharedPref = (activity as AppCompatActivity).getSharedPreferences("user",
            Context.MODE_PRIVATE)
//        val editor = sharedPref.edit()
//            editor.apply{
//                val email = sharedPref.getString("email","kasim_adalan")
//            }
        viewModel.cartFoodList.observe(viewLifecycleOwner){
            if(it!=null){
                val cartAdapter = CartAdapter(requireContext(),it,viewModel,sharedPref.getString("email","kasim_adalan").toString())
                binding.cartAdapter=cartAdapter
            }
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = (activity as AppCompatActivity).getSharedPreferences("user",
            Context.MODE_PRIVATE)
//        val editor = sharedPref.edit()
        val tempViewModel:CartViewModel by viewModels()
        viewModel=tempViewModel
        viewModel.getFoodsFromCart(sharedPref.getString("email","kasim_adalan").toString())
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = (activity as AppCompatActivity).getSharedPreferences("user",
            Context.MODE_PRIVATE)
//        val editor = sharedPref.edit()
        viewModel.getFoodsFromCart(sharedPref.getString("email","kasim_adalan").toString())
    }

}