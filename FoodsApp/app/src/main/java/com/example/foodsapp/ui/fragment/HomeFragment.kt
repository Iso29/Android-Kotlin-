
package com.example.foodsapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.SearchViewBindingAdapter.OnQueryTextSubmit
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.foodsapp.R
import com.example.foodsapp.data.entitiy.Food
import com.example.foodsapp.databinding.FragmentHomeBinding
import com.example.foodsapp.ui.adapter.FoodsAdapter
import com.example.foodsapp.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)
        binding.homeFragment=this
        viewModel.foodList.observe(viewLifecycleOwner){
            val meatList = ArrayList<Food>()
            for(i in it.foods){
                if(i.category.lowercase()=="meals"){
                    meatList.add(i)
                }
            }
            val mealAdapter=FoodsAdapter(requireContext(),meatList,viewModel)
            binding.mealAdapter=mealAdapter

            val drinkList = ArrayList<Food>()
            for(i in it.foods){
                if(i.category.lowercase()=="drinks"){
                    drinkList.add(i)
                }
            }
            val drinkAdapter=FoodsAdapter(requireContext(),drinkList,viewModel)
            binding.drinkAdapter=drinkAdapter

            val desertList = ArrayList<Food>()
            for(i in it.foods){
                if(i.category.lowercase()=="desserts"){
                    desertList.add(i)
                }
            }
            val dessertAdapter=FoodsAdapter(requireContext(),desertList,viewModel)
            binding.dessertAdapter=dessertAdapter
        }

        binding.searchViewHome.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Navigation.findNavController(binding.searchViewHome).navigate(R.id.fromHomeToSearch)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Navigation.findNavController(binding.searchViewHome).navigate(R.id.fromHomeToSearch)
                return false
            }
        })
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel:HomeViewModel by viewModels()
        viewModel=tempViewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAllFoods()
    }
}