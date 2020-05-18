package com.example.partybuddies.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.partybuddies.Fragments.FindPartiesFragment
import com.example.partybuddies.Fragments.YourPartiesFragment
import com.example.partybuddies.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import com.example.partybuddies.Adapters.ViewPageAdapter


class MainActivity : AppCompatActivity() {

    private var db = FirebaseFirestore.getInstance()
    private var img: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val viewPageAdapter: ViewPageAdapter =
            ViewPageAdapter(
                supportFragmentManager
            )
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
        val intent = Intent(this, AddPartyActivity::class.java)
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
