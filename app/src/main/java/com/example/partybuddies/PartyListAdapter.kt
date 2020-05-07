package com.example.partybuddies

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.find_parties_fragment.*
import kotlin.collections.ArrayList

public class PartyListAdapter: RecyclerView.Adapter<PartyListAdapter.PartyHolder> {

    private var parties: ArrayList<Party>
    private lateinit var mInflater: LayoutInflater
    private var users: ArrayList<User> = ArrayList()
    private var db = FirebaseFirestore.getInstance()

    constructor(parties: ArrayList<Party>) : super(){

        this.parties = parties
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PartyListAdapter.PartyHolder {
        this.mInflater = LayoutInflater.from(parent.context)
       var mItemView: View = this.mInflater.inflate(R.layout.party_item, parent, false)
        return PartyHolder(mItemView, this)
    }

    override fun getItemCount(): Int {
        return this.parties.size
    }

    override fun onBindViewHolder(holder: PartyListAdapter.PartyHolder, position: Int) {
        val mCurrent = this.parties[position]
        holder.nameView.text = mCurrent.name
        holder.dateView.text = "${mCurrent.date}"
        holder.locationView.text = "${mCurrent.location}"
//        holder.timeView.text = "Start: ${mCurrent.startTime} End: ${mCurrent.endTime}"
        holder.descriptionView.text = "${mCurrent.description}"
        val users: ArrayList<User> = ArrayList()
        this.db.collection("users").get().addOnSuccessListener { result ->
            for (document in result){
//                this.users.add(document.toObject(User::class.java))
                Log.d("FirebaseHelperr", "${document.toObject(User::class.java).id==mCurrent.userRef}")
                if(document.toObject(User::class.java).id==mCurrent.userRef){
                    Log.d("FirebaseHelper", "Successssssss")
                    Glide.with(holder.imgPerson).load(document.toObject(User::class.java).imgRef).into(holder.imgPerson)
                }
            }
            Log.d("FirebaseHelperr", "Succes")
        }
            .addOnFailureListener {ex ->
                Log.d("FirebaseHelper", ex.message)
            }
//        val users:ArrayList<User> = getUsers()
//        val user: User = users.filter{it.id==mCurrent.userRef}.first()
        Glide.with(holder.img).load(mCurrent.imgRef).into(holder.img)
//        Glide.with(holder.imgPerson).load(user.imgRef).into(holder.imgPerson)

    }

//    private fun getUsers(): ArrayList<User> {
//        val users: ArrayList<User> = ArrayList()
//        this.db.collection("üsers").get().addOnSuccessListener { result ->
//     for (document in result){
//         this.users.add(document.toObject(User::class.java))
//     }
//     Log.d("FirebaseHelper", "Succes")
// }
//     .addOnFailureListener {ex ->
//         Log.d("FirebaseHelper", ex.message)
//     }
//        return users
//    }


    inner class PartyHolder : RecyclerView.ViewHolder {

        lateinit var mAdapter: PartyListAdapter
        lateinit var nameView: TextView
        lateinit var dateView: TextView
        lateinit var locationView: TextView
        lateinit var timeView: TextView
        lateinit var descriptionView: TextView
        lateinit var img:ImageView
        lateinit var imgPerson:ImageView


        constructor(itemView: View, adapter: PartyListAdapter) : super(itemView){
            this.nameView = itemView.findViewById(R.id.textViewPartyName)
            this.dateView = itemView.findViewById(R.id.textViewName)
            this.locationView = itemView.findViewById(R.id.textViewPartyLocation)
//            this.timeView = itemView.findViewById(R.id.textViewDescription)
            this.descriptionView = itemView.findViewById(R.id.textViewPartyDescription)
            this.img = itemView.findViewById(R.id.imageViewPartyPicture)
            this.imgPerson = itemView.findViewById(R.id.imageViewPersonImg)
            this.mAdapter = adapter

            itemView.setOnClickListener { v ->
                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {
                    val clickedDataItem =
                    Toast.makeText(
                        v.context,
                        "You clicked " + parties.get(adapterPosition).name,
                        Toast.LENGTH_SHORT
                    ).show()
                    var intent: Intent = Intent(itemView.context, PartyInfoActivity::class.java)

                    intent.putExtra("party", parties.get(adapterPosition))
                    startActivity(itemView.context, intent, Bundle())
                }
            }

        }


    }


}
