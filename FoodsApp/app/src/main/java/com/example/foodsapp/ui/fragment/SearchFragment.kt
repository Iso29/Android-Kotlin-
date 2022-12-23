package com.example.foodsapp.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.foodsapp.R
import com.example.foodsapp.databinding.FragmentSearchBinding
import com.example.foodsapp.ui.adapter.SearchFoodAdapter
import com.example.foodsapp.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(){
    private lateinit var binding : FragmentSearchBinding
    private lateinit var viewModel : SearchViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_search,container,false)

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                searchFood(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchFood(newText)
                return true
            }

        })

        viewModel.searchedFood.observe(viewLifecycleOwner){
            if(it!=null){
                binding.lottoieAnimationSearch.visibility=View.INVISIBLE
                binding.searchRecyclerView.visibility=View.VISIBLE
                val adapter = SearchFoodAdapter(requireContext(),it,viewModel)
                binding.seacrhAdapter=adapter
            }
            else{
                binding.searchRecyclerView.visibility=View.GONE
                binding.lottoieAnimationSearch.visibility=View.VISIBLE
            }
        }
        val callBack = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.fromSearchToHome)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callBack)

        binding.imageViewBack.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.fromSearchToHome)
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel:SearchViewModel by viewModels()
        viewModel=tempViewModel
    }

    fun searchFood(keyWord:String){
        viewModel.searchFood(keyWord)
    }
}