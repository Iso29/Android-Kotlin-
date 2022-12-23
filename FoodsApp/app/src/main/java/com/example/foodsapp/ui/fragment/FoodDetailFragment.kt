package com.example.foodsapp.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.foodsapp.R
import com.example.foodsapp.data.entitiy.CartFood
import com.example.foodsapp.databinding.FragmentFoodDetailBinding
import com.example.foodsapp.ui.viewmodel.FoodDetailViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodDetailFragment : Fragment() {
    private lateinit var binding : FragmentFoodDetailBinding
    private lateinit var viewModel:FoodDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_food_detail,container,false)
        val sharedPref = (activity as AppCompatActivity).getSharedPreferences("user",
            Context.MODE_PRIVATE)
        val bundle : FoodDetailFragmentArgs by navArgs()
        val recievedFood = bundle.food
        var resultPrice=recievedFood.price
        var cartList = listOf<CartFood>()
        var orderAmount=1
        var resultAmount=1
        binding.textViewPriceDetail.text=resultPrice.toString()+"₼"
        binding.textViewOrderAmountDetail.text=orderAmount.toString()

        viewModel.cartList.observe(viewLifecycleOwner){
            if (it!=null){
                cartList=it
            }
        }

        if (orderAmount==1){
            binding.btnDecrement.isEnabled=false
        }
        binding.btnDecrement.setOnClickListener {
            orderAmount -= 1
            resultAmount=orderAmount
            resultPrice = orderAmount * recievedFood.price
            binding.textViewPriceDetail.text = resultPrice.toString()
            binding.textViewOrderAmountDetail.text = orderAmount.toString()
            if(orderAmount==1){
                it.isEnabled=false
            }
        }


        binding.btnIncrement.setOnClickListener {
            orderAmount+=1
            resultAmount=orderAmount
            resultPrice=orderAmount*recievedFood.price
            if(orderAmount>1){
                binding.btnDecrement.isEnabled=true
            }
            binding.textViewPriceDetail.text=resultPrice.toString()
            binding.textViewOrderAmountDetail.text=orderAmount.toString()
        }

        showImage(recievedFood.image,binding.imageViewFoodDetail)
        binding.chipCategoryName.text=recievedFood.category

        binding.btnAddToCart.setOnClickListener {
            for(f in cartList){
                if(recievedFood.name==f.name){
                    resultAmount=f.orderAmount+resultAmount
                    viewModel.deleteFoodFromCart(f.cartId,sharedPref.getString("email","kasim_adalan").toString())
                }
            }
            insertFoodIntoCart(recievedFood.name,recievedFood.image,resultPrice,recievedFood.category,resultAmount,sharedPref.getString("email","kasim_adalan").toString())
            Snackbar.make(it,"${recievedFood.name} is added to your cart",Snackbar.LENGTH_SHORT).show()
        }

        val callBack = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.fromDetailToHome)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callBack)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = (activity as AppCompatActivity).getSharedPreferences("user",Context.MODE_PRIVATE)
        val tempViewModel : FoodDetailViewModel by viewModels()
        viewModel=tempViewModel
        viewModel.getCartList(sharedPref.getString("email","kasim_adalan").toString())
    }
    fun showImage(imageName:String,imageView: ImageView){
        val url="http://kasimadalan.pe.hu/foods/images/${imageName}"
        Glide.with(this).load(url).override(195,195).into(imageView)
    }

    fun insertFoodIntoCart(name:String,image:String,price:Int,category:String,orderAmount:Int,userName:String){
        viewModel.insertFoodIntoCart(name,image,price,category,orderAmount,userName)
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = (activity as AppCompatActivity).getSharedPreferences("user",Context.MODE_PRIVATE)
        viewModel.getCartList(sharedPref.getString("email","kasim_adalan").toString())
    }
}