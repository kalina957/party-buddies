package com.example.partybuddies.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.partybuddies.Models.Party
import com.example.partybuddies.R
import com.example.partybuddies.Adapters.PartyListAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.your_parties_fragment.*

class YourPartiesFragment: Fragment {

    private lateinit var partiesInfo: ArrayList<String>
    private lateinit var mAdapter: PartyListAdapter
    private lateinit var mRecyclerView: RecyclerView
    private var parties: ArrayList<Party> = ArrayList()
    private var db = FirebaseFirestore.getInstance()

    constructor() : super()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.db.collection("parties").addSnapshotListener{
                snapshot, ex ->
            if (ex !=null){
                Log.e("Firebase", "Exception: $ex.message")
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty){
                this.refreshList()
            }
            else {
                Log.d("Firebase", "Snapshot is null")
            }
        }

        return inflater.inflate(R.layout.your_parties_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }

    private fun refreshList(){
        this.parties = ArrayList()
        this.db.collection("parties").get()
            .addOnSuccessListener { result ->
                for (document in result){
                    var party: Party = document.toObject(
                        Party::class.java)
                    if (party.userRef == "5lKMPWEGgqOKAK6WOo2k"){
                        this.parties.add(party)
                    }
                    for (u in party.usersGoingRef){
                        if (u == "5lKMPWEGgqOKAK6WOo2k"){
                            this.parties.add(party)
                        }
                    }
                }
                this.mRecyclerView = this.listviewYourParties
                this.mAdapter =
                    PartyListAdapter(this.parties)
                this.mRecyclerView.adapter = this.mAdapter
                this.mRecyclerView.layoutManager = LinearLayoutManager(this.mRecyclerView.context)
                Log.d("FirebaseHelper", "Succes")
            }
            .addOnFailureListener {ex ->
                Log.d("FirebaseHelper", ex.message)
            }
    }
}