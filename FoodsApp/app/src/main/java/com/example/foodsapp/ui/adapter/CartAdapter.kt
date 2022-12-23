package com.example.foodsapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodsapp.data.entitiy.CartFood
import com.example.foodsapp.databinding.CartItemsBinding
import com.example.foodsapp.databinding.FragmentCartBinding
import com.example.foodsapp.databinding.HomeFoodDesignBinding
import com.example.foodsapp.ui.viewmodel.CartViewModel

class CartAdapter(var mContext: Context,var cartFoodList:List<CartFood>,var viewModel:CartViewModel,var email:String):
    RecyclerView.Adapter<CartAdapter.CartItemDesign>() {
    inner class CartItemDesign(var binding: CartItemsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemDesign {
        val binding= CartItemsBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return CartItemDesign(binding)
    }

    override fun onBindViewHolder(holder: CartItemDesign, position: Int) {
        val cartFood=cartFoodList.get(position)
        val b = holder.binding
        showImage(cartFood.image,b.imageView2)
        b.textViewFoodNameCart.text=cartFood.name
        b.textViewAmountCart.text=cartFood.orderAmount.toString()
        b.textViewPriceCart.text=cartFood.price.toString()+"₼"

        b.deleteItemIcon.setOnClickListener {
            viewModel.deleteFoodFromCart(cartFood.cartId,email)
        }
    }

    override fun getItemCount(): Int {
        return cartFoodList.size
    }
    fun showImage(imageName:String,view: ImageView){
        val url="http://kasimadalan.pe.hu/foods/images/${imageName}"
        Glide.with(mContext).load(url).override(200,200).into(view)
    }

}