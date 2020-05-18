package com.example.partybuddies.Adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.partybuddies.Activities.ChatLogActivity
import com.example.partybuddies.R
import com.example.partybuddies.Models.User
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CardStackAdapter(private var users: List<User> = emptyList()): RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {
    private var mImageUrls = java.util.ArrayList<String>()
    lateinit var adapterBands : ImageAdapter
    lateinit var parent:ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        this.parent = parent
        return ViewHolder(inflater.inflate(R.layout.buddy, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.name.text = "${user.name}"
        holder.city.text = user.city
        holder.musicGenres.text = user.musicGenres.joinToString()
        for(band in user.BandsRef){
            mImageUrls.add(band)
        }
//        val layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
//        val adapter1 = RecyclerViewAdapter(holder.itemView.context, mImageUrls)
//        holder.rvBands.setAdapter(adapter1)
//        holder.rvBands.setLayoutManager(layoutManager)
        mImageUrls = java.util.ArrayList()

        Glide.with(holder.image)
            .load(user.imgRef)
            .into(holder.image)
        val isExpanded : Boolean = user.isExpanded()
        fun visibility(isExpanded:Boolean): Int{
            if (isExpanded){
                return View.VISIBLE
            }
            else{
                return View.GONE
            }
        }
        holder.expandableLayout.setVisibility(visibility(isExpanded))

        /*holder.image.setOnClickListener(){v ->
            val user: User = users.get(position)
            user.setExpanded(!user.isExpanded())
            notifyItemChanged(position)
            //holder.expandableLayout.setVisibility(visibility(!isExpanded))
            Toast.makeText(v.context, user.name, Toast.LENGTH_SHORT).show()
        }*/

        holder.itemView.setOnClickListener { v ->
            val user: User = users.get(position)
            user.setExpanded(!user.isExpanded())
            notifyItemChanged(position)
            Toast.makeText(v.context, user.name, Toast.LENGTH_SHORT).show()
        }
        holder.btn.setOnClickListener { v ->

            val intent = Intent(v.context, ChatLogActivity::class.java)
            startActivity(v.context, intent, Bundle())
        }
        adapterBands = ImageAdapter(parent.context,user.BandsRef)
        holder.gridBands.adapter = this.adapterBands
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun setSpots(spots: List<User>) {
        this.users = spots
    }

    fun getSpots(): List<User> {
        return users
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.item_name)
        var city: TextView = view.findViewById(R.id.item_city)
        var image: ImageView = view.findViewById(R.id.item_image)
        var expandableLayout: ConstraintLayout = view.findViewById(R.id.expandableLayout1)
        var btn :FloatingActionButton = view.findViewById(R.id.fbtnMessage)
        var musicGenres: TextView = view.findViewById(R.id.tvMoreMusicGenres)
        var gridBands: GridView = view.findViewById(R.id.gridViewBands)
    }
}