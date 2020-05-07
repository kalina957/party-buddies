package com.example.partybuddies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.party_info.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Button
import java.time.LocalDateTime
import java.util.*


class MainActivity : AppCompatActivity() {

    private var db = FirebaseFirestore.getInstance()
    private var img: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val viewPageAdapter: ViewPageAdapter = ViewPageAdapter(supportFragmentManager)
        viewPageAdapter.addFragment(FindPartiesFragment(), "Find Parties")
        viewPageAdapter.addFragment(YourPartiesFragment(), "Your Parties")
        this.viewPagerMainScreen.adapter = viewPageAdapter
        this.tabLayoutMainScreen.setupWithViewPager(this.viewPagerMainScreen)

       /*var cities: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.cities))
        cities.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        this.spinnerCities.adapter = cities*/
    }



//    fun party(view: View){
//        val intent = Intent(this, PartyInfoActivity::class.java)
//        startActivity(intent)
//    }

    fun addParty(view: View){
        val intent = Intent(this, AddParty::class.java)
        startActivity(intent)
    }

   /* fun addUser(view: View) {
        // Create a new user with a first and last name
        val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1815
        )

// Add a new document with a generated ID
        db.collection("users")
            .add(user as Map<String, Any>)
            .addOnSuccessListener() { it ->
                Log.d("FirebaseTest", "DocumentSnapshot added with ID: ${it.id}")

                /*db.collection("data").document("one")
            .update(updates)
            .addOnSuccessListener { Log.d("FirebaseHelper", "DocumentSnapshot succesfully") }
            .addOnFailureListener { e -> Log.d("FirebaseHelper", e.message)}*/
                db.collection("data").add(
                    hashMapOf<String, Any>(
                        "test" to "newValue",
                        "timestamp" to FieldValue.serverTimestamp()
                    )
                )
            }

    }*/
}
