package com.example.tiktaktoe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tiktaktoe.adaptor.FreezAdaptor
import com.example.tiktaktoe.adaptor.PVPAdaptor
import com.example.tiktaktoe.databinding.FragmentPVPBinding
import com.example.tiktaktoe.viewModel.PVPViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PVPFragment : Fragment() {
    private lateinit var binding : FragmentPVPBinding
    private lateinit var viewModel : PVPViewModel
    var countPlayer1 = 0
    var countPlayer2 = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentPVPBinding.inflate(inflater,container,false)
        val bundle : PVPFragmentArgs by navArgs()
        binding.textViewUser1PVP.text = bundle.user1
        binding.textViewUser2PVP.text= bundle.user2

        viewModel.boardList.observe(viewLifecycleOwner){
            val adaptor = PVPAdaptor(requireContext(),it,viewModel)
            binding.adapter=adaptor

            if(checkWin()){
                binding.rvPVP.visibility=View.GONE
                binding.rvFreez.visibility=View.VISIBLE
                val freezAdaptor = FreezAdaptor(requireContext(),it)
                binding.freezAdapter=freezAdaptor
                if(viewModel.firstPlayer){
                    countPlayer2+=1
                    binding.textViewWinnerNamePVP.text=bundle.user2
                }else{
                    countPlayer1+=1
                    binding.textViewWinnerNamePVP.text=bundle.user1
                }
            }

            else if(isDraw()){
                binding.rvPVP.visibility=View.GONE
                binding.rvFreez.visibility=View.VISIBLE
                val freezAdaptor = FreezAdaptor(requireContext(),it)
                binding.freezAdapter=freezAdaptor
                binding.textViewWinnerNamePVP.text="draw"
            }
            binding.textViewUser1ScorePVP.text=countPlayer1.toString()
            binding.textViewUser2ScorePVP.text=countPlayer2.toString()
        }

        binding.btnRemachPVP.setOnClickListener {
            binding.rvFreez.visibility=View.GONE
            binding.rvPVP.visibility=View.VISIBLE
            binding.textViewWinnerNamePVP.text=""
            viewModel.firstPlayer=true
            viewModel.clearBoard()
        }

        binding.btnClrScorePVP.setOnClickListener {
            binding.rvFreez.visibility=View.GONE
            binding.rvPVP.visibility=View.VISIBLE
            binding.textViewWinnerNamePVP.text=""
            countPlayer1=0
            countPlayer2=0
            viewModel.firstPlayer=true
            viewModel.clearBoard()
        }

        val callBack = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel.clearBoard()
                findNavController().navigate(R.id.fromPVPToHome)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callBack)


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel : PVPViewModel by viewModels()
        viewModel = tempViewModel
    }

    fun checkWin():Boolean{
        return viewModel.checkWin()
    }

    fun isDraw():Boolean{
        return viewModel.satelment()
    }


}