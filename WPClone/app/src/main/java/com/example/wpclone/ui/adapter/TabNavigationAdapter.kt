package com.example.wpclone.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.wpclone.ui.fragment.CallFragment
import com.example.wpclone.ui.fragment.ChatFragment
import com.example.wpclone.ui.fragment.StatusFragment

class TabNavigationAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle)
    :FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return if(position==0){
            ChatFragment()
        }else if(position==1){
            CallFragment()
        }else{
            StatusFragment()
        }
    }

}