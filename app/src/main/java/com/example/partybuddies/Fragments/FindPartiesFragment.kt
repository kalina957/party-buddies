package com.example.partybuddies.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.find_parties_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.example.partybuddies.Models.Party
import com.example.partybuddies.R
import com.example.partybuddies.Adapters.PartyListAdapter


class FindPartiesFragment: Fragment {

    private lateinit var partiesInfo: ArrayList<String>
    private lateinit var mAdapter: PartyListAdapter
    private lateinit var mRecyclerView: RecyclerView
    private var parties: ArrayList<Party> = ArrayList()
    private var db = FirebaseFirestore.getInstance()
    private lateinit var selectedChip: Chip
    public var t = ""

    constructor() : super()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //this.refreshList()
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
        return inflater.inflate(R.layout.find_parties_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       /* this.partiesInfo.add("Type: Club \n Location: Eindhoven \n Time: 12:00 to 1:00 \n Description: Party hard")
        this.partiesInfo.add("Type: Festival \n Location: Amsterdam \n Time: 12:00 to 1:00 \n Description: Party hard")
       */
        /*this.parties.add(Party("Club night dance" ,"Club", "Eindhoven 11", "12:40 till 1:00", "Party hard"))
        this.parties.add(Party("Club night dance" ,"Club", "Eindhoven 11", "12:40 till 1:00", "Party hard"))
        this.parties.add(Party("Club night dance" ,"Club", "Eindhoven 11", "12:40 till 1:00", "Party hard"))
        this.parties.add(Party("Club night dance" ,"Club", "Eindhoven 11", "12:40 till 1:00", "Party hard"))
        this.parties.add(Party("Club night dance" ,"Club", "Eindhoven 11", "12:40 till 1:00", "Party hard"))*/
        /*val doc = this.db.collection("parties").document()
        val party: Party = Party(doc.id,"Club night dance" ,"Club", "Eindhoven 11", "12:40 till 1:00", "Party hard")
        doc.set(party)*/
        /*if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(this.toolbarFilter)
        }
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)*/
        //this.refreshList()
        this.selectedChip = this.chipFilter
        this.setChips()
    }

    private fun setChips(){
        this.chipToday.setOnClickListener {
            if (this.chipToday.isChecked){
                this.selectedChip = this.chipToday
                Log.d("testtest", "succes")
            }
            else{
                this.selectedChip = this.chipFilter
            }
            refreshList()
        }

        this.chipTomorrow.setOnClickListener{
            if (this.chipTomorrow.isChecked){
                this.selectedChip = this.chipTomorrow
            }
            else{
                this.selectedChip = this.chipFilter
            }
            refreshList()
        }

        this.chipWeek.setOnClickListener{
            if (this.chipWeek.isChecked){
                this.selectedChip = this.chipWeek
            }
            else{
                this.selectedChip = this.chipFilter
            }
            refreshList()
        }
        this.chipDate.setOnClickListener{

            if (this.chipDate.isChecked){
                this.selectedChip = this.chipDate
                DatePickerFragment2(this).show(requireFragmentManager(), "datePicker")
            }
            else{
                this.selectedChip = this.chipFilter
                refreshList()
            }

        }
    }


    public fun refreshList(){
        this.parties = ArrayList()

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        /*var year = SimpleDateFormat("yyyy")
        var day = SimpleDateFormat("dd").format(Date()).toInt()
        var month = SimpleDateFormat("MM").format(Date()).toInt()*/
        val currentDate = sdf.format(Date())
       /* var myString = DateFormat.getDateInstance().format(Date())
        val df = DateFormat.getDateInstance().parse("2019-2-2")*/

        //var myDate = df.parse(myString)
        var t = Date()
        t.date = t.date + 1
        var tomorrow = sdf.format(t)

       /* val currentCalendar = Calendar.getInstance()
        val week = currentCalendar.get(Calendar.WEEK_OF_YEAR)
        val year1 = currentCalendar.get(Calendar.YEAR)
        val targetCalendar = Calendar.getInstance()*/

        //Log.d("testtest777",df.toString())
        this.db.collection("parties").get()
            .addOnSuccessListener { result ->
                for (document in result){
                    var p = document.toObject(Party::class.java)
                    if (p.userRef != "5lKMPWEGgqOKAK6WOo2k"){
                        //var t = p.date.split('-')
                        /*var date = Date()
                        date.year = t.get(0).toInt()
                        date.month = t.get(1).toInt()
                        date.date = t.get(2).toInt()

                        Log.d("testtest", date.toString())
                        targetCalendar.time = date
                        val targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR)
                        val targetYear = targetCalendar.get(Calendar.YEAR)*/
                        Log.d("testtest", this.t)
                        when(this.selectedChip){

                            this.chipToday -> if (p.date == currentDate){ this.parties.add(p)}
                            this.chipTomorrow -> if (p.date == tomorrow) { this.parties.add(p)}
                            //this.chipWeek -> if (week == targetWeek && year1 == targetYear){this.parties.add(p)}
                            this.chipDate -> if (p.date == this.t) {this.parties.add(p)}
                            this.chipFilter -> this.parties.add(p)}
                            }
                        }
                        //this.parties.add(document.toObject(Party::class.java))
                this.mRecyclerView = this.listViewParties
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