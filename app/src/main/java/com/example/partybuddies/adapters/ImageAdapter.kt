package com.example.partybuddies.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.partybuddies.R

class ImageAdapter(var context:Context,var images:ArrayList<String>) : BaseAdapter() {
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var gridView: View? = p1
        if (gridView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            gridView = inflater.inflate(R.layout.band_image_layout, null)

        }
        val img = gridView!!.findViewById(R.id.imgBand) as ImageView
        Glide.with(img).load(images[p0]).into(img)
        return gridView
    }

    override fun getItem(p0: Int): Any {
        return images.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return  return p0.toLong()
    }

    override fun getCount(): Int {
        return images.count()
    }


}