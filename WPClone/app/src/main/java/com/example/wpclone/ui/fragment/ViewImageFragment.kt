package com.example.wpclone.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wpclone.R
import com.example.wpclone.databinding.FragmentViewImageBinding
import com.example.wpclone.utils.Constants

class ViewImageFragment : Fragment() {
    private lateinit var binding : FragmentViewImageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewImageBinding.inflate(inflater,container,false)
        init()
        return binding.root
    }

    private fun init(){
        binding.apply {
            if(Constants.IMAGE_BITMAP!=null){
                zoomImageView.setImageBitmap(Constants.IMAGE_BITMAP)
            }
            backArrowImageView.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }


}