package com.example.foodsapp.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodsapp.R
import com.example.foodsapp.data.entitiy.Food
import com.example.foodsapp.data.entitiy.User
import com.example.foodsapp.databinding.SeachItemBinding
import com.example.foodsapp.ui.fragment.SearchFragmentDirections
import com.example.foodsapp.ui.viewmodel.SearchViewModel

class SearchFoodAdapter(var mContext: Context, var foodList:List<Food>, var viewModel:SearchViewModel) :
    RecyclerView.Adapter<SearchFoodAdapter.SearchItemDesign>() {
    inner class SearchItemDesign(var binding : SeachItemBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemDesign {
        val binding:SeachItemBinding=
            DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.seach_item,parent,false)
        return SearchItemDesign(binding)
    }

    override fun onBindViewHolder(holder: SearchItemDesign, position: Int) {
        val food = foodList.get(position)
        val b =holder.binding
        showImage(food.image,b.imageViewFoodSearch)
        b.textViewFoodNameSeacrh.text=food.name
        b.textViewFoodPriceSearch.text=food.price.toString()+"₼"
        b.chipCategorySearchItem.text=food.category

        b.carViewSearchItem.setOnClickListener{
            val transition = SearchFragmentDirections.fromSearchToDetail(food = food)
            Navigation.findNavController(it).navigate(transition)
        }

    }

    override fun getItemCount(): Int {
        Log.e("size",foodList.size.toString())
        return foodList.size
    }

    fun showImage(imageName:String,imageView: ImageView){
        val url="http://kasimadalan.pe.hu/foods/images/${imageName}"
        Glide.with(mContext).load(url).override(205,205).into(imageView)
    }
}