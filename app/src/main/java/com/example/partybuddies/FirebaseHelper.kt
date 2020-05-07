package com.example.partybuddies

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class FirebaseHelper {

   // private var db = FirebaseFirestore.getInstance()

    constructor(){

    }

   /* fun addUser(message: String){
        val user = hashMapOf(
            "firstName" to "testFirstName",
            "lastName" to "testLastName",
            "YearOfBirth" to 1998
        )
        this.db.collection("users").add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("FirebaseHelper", "testSuccse ${documentReference.id}")
            }
            .addOnFailureListener {e ->
                Log.d("FirebaseHelper", "error" + e.message)
            }
    }*/
}