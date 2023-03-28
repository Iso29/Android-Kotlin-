package com.example.wpclone.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.wpclone.R

class CustomSpinnerAdapter(val mContext: Context, val spinnerList:List<String>) : BaseAdapter() {
    override fun getCount(): Int {
        return spinnerList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rootView = LayoutInflater.from(mContext).inflate(R.layout.spinner_item,parent,false)
        val textView = rootView.findViewById<TextView>(R.id.textViewItemName)
        textView.text = spinnerList.get(position)
        return rootView
    }
}