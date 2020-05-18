package com.example.partybuddies.Models

import java.io.Serializable

public class Coordinate: Serializable{

     var latitude: Double = 0.0
    var longitude: Double = 0.0

    constructor()

    constructor(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
    }
}