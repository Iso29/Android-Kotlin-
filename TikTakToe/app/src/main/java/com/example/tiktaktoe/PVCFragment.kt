package com.example.tiktaktoe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tiktaktoe.adaptor.FreezAdaptor
import com.example.tiktaktoe.adaptor.PVCAdaptor
import com.example.tiktaktoe.adaptor.PVPAdaptor
import com.example.tiktaktoe.databinding.FragmentPVCBinding
import com.example.tiktaktoe.viewModel.PVCViewModel
import com.example.tiktaktoe.viewModel.PVPViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random
@AndroidEntryPoint
class PVCFragment : Fragment() {
    private lateinit var binding : FragmentPVCBinding
    private lateinit var viewModel : PVCViewModel
    var countPlayer1 = 0
    var countCompuet = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentPVCBinding.inflate(inflater,container,false)
        val bundle : PVCFragmentArgs by navArgs()
        binding.textViewUser1PVC.text=bundle.user1

        viewModel.boardList.observe(viewLifecycleOwner){
            val adaptor = PVCAdaptor(requireContext(),it,viewModel)
            binding.adaptorPVC=adaptor

            if(checkWin()){
                binding.rvPVC.visibility=View.GONE
                binding.rvFreezPvc.visibility=View.VISIBLE
                val freezAdaptor = FreezAdaptor(requireContext(),it)
                binding.freezAdaptor=freezAdaptor
                if(viewModel.firstPlayer){
                    countCompuet+=1
                    binding.textViewWinnerPVC.text="computer win"
                }else{
                    countPlayer1+=1
                    binding.textViewWinnerPVC.text="${bundle.user1} win"
                }
            }

            else if(isDraw()){
                binding.rvPVC.visibility=View.GONE
                binding.rvFreezPvc.visibility=View.VISIBLE
                val freezAdaptor = FreezAdaptor(requireContext(),it)
                binding.freezAdaptor=freezAdaptor
                binding.textViewWinnerPVC.text="draw"
            }else{
                while(!viewModel.firstPlayer){
                    var n = Random.nextInt(0,9)
                    viewModel.changeXorY(n,"X")
                }
            }

            binding.textViewUser1ScorePVC.text=countPlayer1.toString()
            binding.textViewComputerScorePVC.text=countCompuet.toString()
        }

        binding.btnRemachPVC.setOnClickListener {
            binding.rvFreezPvc.visibility=View.GONE
            binding.rvPVC.visibility=View.VISIBLE
            binding.textViewWinnerPVC.text=""
            viewModel.firstPlayer=true
            viewModel.clearBoard()
        }

        binding.btnClearScorePVC.setOnClickListener {
            binding.rvFreezPvc.visibility=View.GONE
            binding.rvPVC.visibility=View.VISIBLE
            binding.textViewWinnerPVC.text=""
            countPlayer1=0
            countCompuet=0
            viewModel.firstPlayer=true
            viewModel.clearBoard()
        }

        val callBack = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel.clearBoard()
                findNavController().navigate(R.id.fromPVCToHome)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callBack)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel : PVCViewModel by viewModels()
        viewModel = tempViewModel
    }

    fun checkWin():Boolean{
        return viewModel.checkWin()
    }

    fun isDraw():Boolean{
        return viewModel.satelment()
    }
}