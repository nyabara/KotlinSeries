package com.example.kotlinseries.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.kotlinseries.R


class CustomSpinnerAdapter(val context: Context, val flags:List<Int>, val houseTypes:List<String>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return flags.count()
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View? {
        val layoutInflater = LayoutInflater.from(viewGroup?.context)
        var view: View? = convertView
        view = layoutInflater.inflate(R.layout.custom_spinner_item, null)
        val icon = view?.findViewById<ImageView>(R.id.imageView)
        val names = view?.findViewById<TextView>(R.id.textView)


        icon?.setImageResource(flags[position])
        names?.text = houseTypes[position]
        return view

    }
}