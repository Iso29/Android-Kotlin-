package com.example.foodsapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodsapp.R
import com.example.foodsapp.data.entitiy.Food
import com.example.foodsapp.data.entitiy.User
import com.example.foodsapp.databinding.HomeFoodDesignBinding
import com.example.foodsapp.ui.fragment.HomeFragmentDirections
import com.example.foodsapp.ui.viewmodel.HomeViewModel

class FoodsAdapter(var mContext:Context,var foods:List<Food>,var viewModel:HomeViewModel):RecyclerView.Adapter<FoodsAdapter.HomeFoodDesign>() {

    inner class HomeFoodDesign(var binding: HomeFoodDesignBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFoodDesign {
//        val binding:HomeFoodDesignBinding=
//            DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.home_food_design,parent,false)
        val binding=HomeFoodDesignBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return HomeFoodDesign(binding)
    }

    override fun onBindViewHolder(holder: HomeFoodDesign, position: Int) {
        val food=foods.get(position)
        val b = holder.binding
        b.textViewFoodName.text="${food.name}"
        b.textViewFoodPrice.text="${food.price}"
        showImage(food.image,b.imageViewFood)
        b.cardViewFood.setOnClickListener{
            val transition = HomeFragmentDirections.fromHomeToDetail(food=food)
            Navigation.findNavController(it).navigate(transition)
        }
    }

    override fun getItemCount(): Int {
        return foods.size
    }

    fun showImage(imageName:String,view:ImageView){
        val url="http://kasimadalan.pe.hu/foods/images/${imageName}"
        Glide.with(mContext).load(url).override(200,200).into(view)
    }

}

