package com.example.partybuddies.Models

import android.media.Image
import com.google.firebase.firestore.model.value.IntegerValue
import java.io.Serializable
import java.sql.Time

class Party: Serializable{

    lateinit var id: String
    lateinit var name: String
    lateinit var type: String
    lateinit var location: String
    lateinit var startTime: String
    lateinit var endTime: String
    lateinit var imgRef: String
    lateinit var description: String
    var peopleQty: Int =0
    lateinit var date: String
    lateinit var userRef: String;
    var usersGoingRef: ArrayList<String> = ArrayList()

    constructor()

    constructor(id: String, name: String, date: String, type: String, location: String, startTime: String, endTime: String, imgRef: String,description: String, peopleQty: Int, userRef: String, usersGoingRef: ArrayList<String>) {
        this.name = name
        this.type = type
        this.location = location
        this.startTime = startTime
        this.endTime = endTime
        this.imgRef = imgRef
        this.description = description
        this.peopleQty = peopleQty
        this.date = date
        this.userRef = userRef
        this.id = id
        this.usersGoingRef = usersGoingRef

    }
}