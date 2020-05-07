package com.example.kalina.membersdemo.dataObj

import android.media.Image
import com.google.firebase.firestore.model.value.IntegerValue
import java.sql.Time

class DataLv {
    lateinit var name: String
    lateinit var imgRef: String
    lateinit var city: String
    private var expanded: Boolean = false

    fun isExpanded(): Boolean {
        return this.expanded
    }
    fun setExpanded(expanded:Boolean) {
        this.expanded = expanded
    }
    constructor()
    constructor(city: String, name: String, imgRef: String){
        this.name = name
        this.imgRef = imgRef
        this.city = city
        this.expanded = false
    }
}
