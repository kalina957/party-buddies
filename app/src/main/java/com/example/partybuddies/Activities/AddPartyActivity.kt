package com.example.partybuddies.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.example.partybuddies.Fragments.DatePickerFragment
import com.example.partybuddies.Fragments.TimePickerFragment
import com.example.partybuddies.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_party.*

class AddPartyActivity : AppCompatActivity() {

    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_party)

        this.db.collection("types").addSnapshotListener{
                snapshot, ex ->
            if (ex !=null){
                Log.e("Firebase", "Exception: $ex.message")
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty){
                var types = ArrayList<String>()
                this.db.collection("types").get()
                    .addOnSuccessListener { result ->
                        for (document in result){
                            types.add(document.get("name").toString())
                        }
                        var spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)
                        this.spinnerPartyType1.adapter = spinnerAdapter
                        Log.d("FirebaseHelper", "Succes")
                    }
                    .addOnFailureListener {ex ->
                        Log.d("FirebaseHelper", ex.message)
                    }
            }
            else {
                Log.d("Firebase", "Snapshot is null")
            }
        }
    }

    fun showTimePicker(view: View) {
        if (view.id == this.textViewStartTime.id) {
            TimePickerFragment(true, this.textViewStartTime).show(
                supportFragmentManager,
                "timePicker"
            )
        } else {
            TimePickerFragment(false, this.textViewEndTime).show(
                supportFragmentManager,
                "timePicker"
            )
        }
    }

    fun showDatePicker(view: View) {
        DatePickerFragment(this.textViewCdate).show(supportFragmentManager, "datePicker")
    }

    fun addParty(view: View) {
       val e = ""
        var check = false
        when(e){
            this.editTextPartyName1.text.toString() ->  {this.editTextPartyName.error = "PartyName is required" ; check = true}
            this.editTextLocation1.text.toString() ->  {this.editTextLocation.error = "Location is required"; check = true}
            this.editTextAmountOfPeople1.text.toString() ->  {this.editTextAmountOfPeople.error = "Quantity is required"; check = true}
            this.textViewCdate.text.toString() -> {this.textViewCdate.error = "Date is required"; check = true}
            this.textViewEndTime.text.toString() ->  {this.textViewEndTime.error = "EndTime is required"; check = true}
            this.textViewStartTime.text.toString() ->  {this.textViewStartTime.error = "StartTime is required"; check = true}
        }
        if (!check){
            //this.editTextPartyName.error = "PartyName is required"
            var docId = db.collection("parties").document().id
            val party = hashMapOf(
                "id" to docId,
                "name" to this.editTextPartyName1.text.toString(),
                "type" to this.spinnerPartyType1.selectedItem.toString(),
                "location" to this.editTextLocation1.text.toString(),
                "peopleQty" to this.editTextAmountOfPeople1.text.toString().toInt(),
                "date" to this.textViewCdate.text.toString(),
                "startTime" to this.textViewStartTime.text.toString().substring(7),
                "endTime" to this.textViewEndTime.text.toString().substring(5),
                "description" to this.editTextDescription1.text.toString(),
                "userRef" to "5lKMPWEGgqOKAK6WOo2k",
                "imgRef" to "https://firebasestorage.googleapis.com/v0/b/party-buddies.appspot.com/o/poolparty.jpg?alt=media&token=27ac5560-9820-44dc-9620-4d363110e00a"
            )
            // Add a new document with a generated ID
            db.collection("parties").document(docId)
                .set(party as Map<String, Any>)
                .addOnSuccessListener() { it ->
                    Log.d("Firebase", "Parties has been added with ID: ${docId}")

                    db.collection("parties").document("one")
                /*.update(updates)
                .addOnSuccessListener { Log.d("FirebaseHelper", "DocumentSnapshot succesfully") }
                .addOnFailureListener { e -> Log.d("FirebaseHelper", e.message)}*/
                    this.finish()}
        }

    }

        }


    //}

