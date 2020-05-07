package com.example.partybuddies.adapters

import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.partybuddies.*

class UserListAdapter: RecyclerView.Adapter<UserListAdapter.UserHolder>  {

    private var users: ArrayList<User>
    private var user: User
    private lateinit var mInflater: LayoutInflater
    lateinit var recyclerView:RecyclerView
    private var mImageUrls = java.util.ArrayList<String>()


    constructor(users: ArrayList<User>, user: User) : super(){
        this.users = users
        this.user = user
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserHolder {
        this.mInflater = LayoutInflater.from(parent.context)
        var mItemView: View = this.mInflater.inflate(R.layout.lv_items, parent, false)
        return UserHolder(mItemView, this)

    }

    override fun getItemCount(): Int {
        return this.users.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val mCurrent = this.users[position]
        holder.nameView.text = mCurrent.name + ", " + mCurrent.city
        val firstThreeGenres : ArrayList<String> = ArrayList()
        var x = 0
        Log.d("FirebaseT", mCurrent.musicGenres.toString())
       while(x<2){
            firstThreeGenres.add(mCurrent.musicGenres.get(x))
            x++
        }
        if (mCurrent.id != "5lKMPWEGgqOKAK6WOo2k"){
            var results: FloatArray = FloatArray(5)
            Location.distanceBetween(this.user.coordinate.latitude, this.user.coordinate.longitude, mCurrent.coordinate.latitude, mCurrent.coordinate.longitude, results)
            var r = "%.2f".format((results.get(0) / 1000))
            holder.interestView.text = "${firstThreeGenres.joinToString()}.. ${r}km"
        }
        else{
            holder.interestView.text = firstThreeGenres.joinToString()
        }
        holder.moreMusicGenres.text = ( mCurrent.musicGenres as ArrayList<String>).joinToString()
        mImageUrls = java.util.ArrayList()
        for(band in mCurrent.BandsRef){
            mImageUrls.add(band)
        }
        val layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        val adapter1 = RecyclerViewAdapter(holder.itemView.context, mImageUrls)
        holder.recyclerView.setAdapter(adapter1)
        holder.recyclerView.setLayoutManager(layoutManager)

      /*  holder.dateView.text = "${mCurrent.date}"
        holder.locationView.text = "${mCurrent.location}"
        holder.timeView.text = "Start: ${mCurrent.startTime} End: ${mCurrent.endTime}"
        holder.descriptionView.text = "${mCurrent.description}"*/
        Glide.with(holder.img).load(mCurrent.imgRef).into(holder.img)


        if (mCurrent.id == "5lKMPWEGgqOKAK6WOo2k"){
            holder.notExpandable.setBackgroundResource(R.color.colorBlueIsh)
            holder.expandableLayout.setBackgroundResource(R.color.colorBlueIsh)
        }
        else{
            holder.notExpandable.setBackgroundResource(R.color.colorWhite)
            holder.expandableLayout.setBackgroundResource(R.color.colorWhite)
        }
        val isExpanded : Boolean = this.users.get(position).isExpanded()
        fun visibility(isExpanded:Boolean): Int{
            if (isExpanded){
                holder.interestView.text = ""

                return View.VISIBLE
            }
            else{
                return View.GONE
            }
        }
        holder.expandableLayout.setVisibility(visibility(isExpanded))

    }


    inner class UserHolder : RecyclerView.ViewHolder {

        lateinit var mAdapter: UserListAdapter
        lateinit var nameView: TextView
        lateinit var interestView: TextView
        lateinit var cityView: TextView
        lateinit var img: ImageView
        lateinit var expandableLayout: ConstraintLayout
        lateinit var notExpandable: ConstraintLayout
        lateinit var btnMessage: Button
        lateinit var moreMusicGenres: TextView
        lateinit var recyclerView: RecyclerView

        constructor(itemView: View, adapter: UserListAdapter) : super(itemView){
            this.nameView = itemView.findViewById(R.id.textViewUserName)
            this.interestView = itemView.findViewById(R.id.textViewInterests)
            this.img = itemView.findViewById(R.id.imageViewUser)
            this.expandableLayout = itemView.findViewById(R.id.expandableLayout)
            this.notExpandable = itemView.findViewById(R.id.constraintSecondary)
            this.btnMessage = itemView.findViewById(R.id.btnMessage)
            this.moreMusicGenres = itemView.findViewById(R.id.tvMoreMusicGenres)
            this.recyclerView = itemView.findViewById(R.id.rvBands)
            //this.cityView = itemView.findViewById(R.id.textViewUserCity)
            this.notExpandable.setOnClickListener(){v ->
                val user: User = users.get(adapterPosition)
                user.setExpanded(!user.isExpanded())
                notifyItemChanged(adapterPosition)
            }

//
            this.mAdapter = adapter

            /*itemView.setOnClickListener { v ->
                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {
                    val clickedDataItem =
                        Toast.makeText(
                            v.context,
                            "You clicked " + users.get(adapterPosition).name,
                            Toast.LENGTH_SHORT
                        ).show()
                    /*var intent: Intent = Intent(itemView.context, PartyInfoActivity::class.java)

                    intent.putExtra("party", users.get(adapterPosition))
                    ContextCompat.startActivity(itemView.context, intent, Bundle())*/
                }
            }*/

        }


    }
//    private fun getImages() {
//
//
//        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg")
//        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg")
//        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg")
//        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg")
//        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg")
//        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg")
//        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg")
//        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg")
//        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg")
//
//    }


}