package com.example.partybuddies

import android.media.Image
import com.google.firebase.firestore.model.value.IntegerValue
import java.io.Serializable
import java.sql.Time

class User: Serializable {

    lateinit var id: String
    lateinit var name: String
    lateinit var city: String
    lateinit var imgRef: String
    var musicGenres: ArrayList<String> = ArrayList<String>()
    var BandsRef: ArrayList<String> = ArrayList()
    lateinit var coordinate: Coordinate
    private var expanded: Boolean = false

    fun isExpanded(): Boolean {
        return this.expanded
    }
    fun setExpanded(expanded:Boolean) {
        this.expanded = expanded
    }
    constructor()

    constructor(BandsRef: ArrayList<String>,userMusicGenres: ArrayList<String>,id: String,name: String, city: String, imgRef: String, coordinate: Coordinate) {
        this.BandsRef = BandsRef
        this.musicGenres = userMusicGenres
        this.name = name
        this.city = city
        this.imgRef = imgRef
        this.id = id
        this.coordinate = coordinate
    }
}