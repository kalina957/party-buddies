package com.example.partybuddies.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.kalina.membersdemo.dataObj.DataLv
import com.example.partybuddies.R

class AdapterLv(var context:Context,var users:ArrayList<DataLv>): BaseAdapter() {

    lateinit var img:ImageView
    lateinit var city:TextView
    lateinit var name:TextView

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.lv_items,p2,false)
        img = view.findViewById(R.id.imageViewUser)
       // city = view.findViewById(R.id.textViewUserCity)
        name = view.findViewById(R.id.textViewUserName)
//        img.setImageResource(data.get(p0).img)
        city.text= users.get(p0).city
        name.text = users.get(p0).name


        Glide.with(img).load(users.get(p0).imgRef).into(img);
        return view
    }

    override fun getItem(p0: Int): Any {
        return users.get(p0)
    }

    override fun getItemId(p0: Int): Long {
       return p0.toLong()
    }

    override fun getCount(): Int {
        return users.size
    }
}